package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.User;

/**
 * Defines the interface for User object persistence.
 */
public interface UserDAO {
    /**
     * Retrieves a {@linkplain User user} by internal ID.
     * @param id The ID to retrieve.
     * @return {@link User user} with matching ID.
     */
    User getUser(int id);

    /**
     * Retrieves a {@linkplain User user} by their case-insensitive username.
     * @param username The username to search for.
     * @return {@link User User} with matching username.
     */
    User getUser(String username);

    /**
     * Creates and persists a {@linkplain User user}.
     * @param user Unique username for {@link User user}.
     * @param password Authentication password for {@link User user}.
     * @return Newly created {@link User user}.
     */
    User createUser(String user, String password);
}
