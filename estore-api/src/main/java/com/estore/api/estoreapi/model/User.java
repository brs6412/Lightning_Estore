package com.estore.api.estoreapi.model;

/**
 * Represents an authenticated user entity.
 */
public class User {
    public enum Role {
        /**
         * A type of user whose primary purpose is to browse/purchase jerseys.
         * Has no privileges regarding the management of the store.
         */
        CUSTOMER,

        /**
         * A type of user whose main concern is managing the shop.
         * They have access to various administrative utilities
         * including managing inventory.
         */
        ADMIN
    };

    /**
     * Internal unique ID associated with this user.
     */
    private int id;

    private String username;

    /**
     * The password associated with this user account.
     * Generally speaking for security reasons you wouldn't store the actual password.
     * But that isn't important in the scope of this project.
     */
    private String password;

    /**
     * Permissions level for this user.
     */
    private Role role = Role.CUSTOMER;

    /**
     * Dummy constructor for JSON serialization.
     */
    public User() {};

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public Role getRole() { return this.role; }

    @Override public boolean equals(Object other) {
        if (!(other instanceof User)) return false;
        return ((User)other).getUsername().equals(this.username);
    }

    @Override public int hashCode() { return this.id; }
}
