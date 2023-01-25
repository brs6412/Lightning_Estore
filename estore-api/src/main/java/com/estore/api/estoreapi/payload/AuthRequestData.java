package com.estore.api.estoreapi.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Payload used for user information in
 * login/register requests.
 */
public class AuthRequestData {
    private String username;
    private String password;

    public AuthRequestData(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
}
