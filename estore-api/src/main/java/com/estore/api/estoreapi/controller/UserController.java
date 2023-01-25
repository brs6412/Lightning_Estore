package com.estore.api.estoreapi.controller;

import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.security.service.UserDetailsInstance;

/**
 * Handles the REST API requests for the User resource.
 */
@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    /**
     * Responds to the GET request for a user retrieving their account details.
     * @return ResponseEntity with {@link UserDetailsInstance user details} and HTTP status of OK. 
     */
    @GetMapping("me")
    protected ResponseEntity<UserDetailsInstance> getUserDetails(@AuthenticationPrincipal UserDetailsInstance details) {
        LOG.info("GET /users/me");
        
        return ResponseEntity.ok(details);
    }
}
