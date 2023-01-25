package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.security.service.UserDetailsInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the User Controller class
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDetailsInstance details;

    @BeforeEach
    void setupUserController() {
        userController = new UserController();
        this.details = UserDetailsInstance.build(new User(1, "user", "password"));
    }

    @Test
    void testGetUserDetails() {
        ResponseEntity<UserDetailsInstance> response = this.userController.getUserDetails(this.details);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.details, response.getBody());
    }
}
