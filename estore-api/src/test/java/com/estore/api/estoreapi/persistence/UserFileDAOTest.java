package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test the UserFileDAO class
 */
@Tag("Persistence-tier")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] users;
    ObjectMapper mapper;

    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mapper = mock(ObjectMapper.class);

        users = new User[2];

        users[0] = new User(0, "uno", null);
        users[1] = new User(1, "dos", null);

        when(mapper.readValue(new File("users.json"), User[].class))
            .thenReturn(users);
        userFileDAO = new UserFileDAO("users.json", mapper);
    }

    @Test
    public void testGetUserByName() {
        User user = userFileDAO.getUser("uno");

        assertNotNull(user);
        assertEquals(users[0], user);
    }

    @Test
    public void testGetUserByNameNotExists() {
        User user = userFileDAO.getUser("tres");
        assertNull(user);
    }

    @Test
    public void testGetUserByID() {
        User user = userFileDAO.getUser(0);

        assertNotNull(user);
        assertEquals(users[0], user);
    }

    @Test
    public void testGetUserByIDNotExists() {
        User user = userFileDAO.getUser(0xDEADBEEF);
        assertNull(user);
    }

    @Test
    public void testCreateUser() {
        User user = assertDoesNotThrow(() ->
            userFileDAO.createUser("tres", null), "Unexpected exception thrown");
        
        assertNotNull(user);
        assertEquals("tres", user.getUsername());
    }

    @Test
    public void testCreateUserAlreadyExists() {
        User user = assertDoesNotThrow(() ->
            userFileDAO.createUser("uno", null), "Unexpected exception thrown");
        assertNull(user);
    }
}
