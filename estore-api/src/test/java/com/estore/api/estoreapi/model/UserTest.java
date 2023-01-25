package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the CartItem class
 */
@Tag("Model-tier")
public class UserTest {
    @Test
    public void testUsernameEquality() {
        User l = new User(1, "username", "password");
        User r = new User(1, "username", "password");

        assertTrue(l.equals(r));
    }

    @Test
    public void testEqualityDifferingTypes() {
        User l = new User(1, "username", "password");;
        assertFalse(l.equals("User"));
    }
}
