package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.payload.AuthRequestData;
import com.estore.api.estoreapi.payload.AuthResponseData;
import com.estore.api.estoreapi.persistence.CartDAO;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.security.SecurityConfig;
import com.estore.api.estoreapi.security.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test the Authentication Controller class
 */
@Tag("Controller-tier")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest({ AuthController.class, UserController.class })
@Import({ AuthController.class, UserController.class, JWT.class })
public class AuthControllerTest {
    @Autowired WebApplicationContext context;
    @Autowired PasswordEncoder encoder;
    @Autowired AuthenticationManager manager;

    @Autowired JWT jwt;
    @MockBean UserDAO userDAO;
    @MockBean CartDAO cartDAO;

    @Autowired private MockMvc mvc;

    private ObjectMapper mapper;

    @BeforeEach
    private void setup() {
        this.mapper = new ObjectMapper();
        
        User user = new User(1, "user", this.encoder.encode("password"));

        when(this.userDAO.getUser("user")).thenReturn(user);
        when(this.userDAO.getUser(1)).thenReturn(user);
    }

    @Test
    void testLogin() throws Exception {
        AuthRequestData data = new AuthRequestData("user", "password");
        MvcResult result = this.mvc.perform(
            post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(data)))
            .andExpect(status().isOk())
            .andReturn();

        AuthResponseData response = this.mapper.readValue(result.getResponse().getContentAsString(), AuthResponseData.class);
        
        this.mvc.perform(
            get("/users/me")
            .header("Authorization", "Bearer " + response.getToken())
        ).andExpect(status().isOk());
    }

    @Test
    void testFailedAuthenticationEmptyToken() throws Exception {
        this.mvc.perform(
            get("/users/me")
            .header("Authorization", "")
        ).andExpect(status().isForbidden());
    }

    @Test
    void testFailedAuthenticationInvalidToken() throws Exception {
        this.mvc.perform(
            get("/users/me")
            .header("Authorization", "whatsatoken")
        ).andExpect(status().isForbidden());
    }

    @Test
    void testLoginFail() throws Exception {
        AuthRequestData data = new AuthRequestData("user", "whatsthedealwithairlinefood");
        this.mvc.perform(
            post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(data)))
            .andExpect(status().isForbidden());
    }

    @Test
    void testLoginDoesntExist() throws Exception {
        AuthRequestData data = new AuthRequestData("new_user", "whatsthedealwithairlinefood");
        this.mvc.perform(
            post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(data)))
            .andExpect(status().isForbidden());
    }

    @Test
    void testRegisterUserExists() throws Exception {
        AuthRequestData data = new AuthRequestData("user", "password");
        this.mvc.perform(
            post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(data)))
            .andExpect(status().isConflict());
    }

    @Test
    void testRegisterUsernameValidation() throws Exception {
        AuthRequestData data = new AuthRequestData("u", "password");
        this.mvc.perform(
            post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(data)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterInvalidUsername() throws Exception {
        AuthRequestData data = new AuthRequestData(null, "password");
        this.mvc.perform(
            post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(data)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterInvalidPassword() throws Exception {
        AuthRequestData data = new AuthRequestData("new_user", null);
        this.mvc.perform(
            post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(data)))
            .andExpect(status().isBadRequest());
    }
}
