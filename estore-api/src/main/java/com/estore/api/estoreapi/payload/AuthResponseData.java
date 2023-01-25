package com.estore.api.estoreapi.payload;

import com.estore.api.estoreapi.model.User.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication information used in response to successful
 * login requests.
 */
public class AuthResponseData {
    private String username;
    private Role role;
    private String token;

    public AuthResponseData(@JsonProperty("username") String username, @JsonProperty("role") Role role, @JsonProperty("token") String token) {
        this.username = username;
        this.role = role;
        this.token = token;
    }

    public String getUsername() { return this.username; }
    public Role getRole() { return this.role; }
    public String getToken() { return this.token; }
}
