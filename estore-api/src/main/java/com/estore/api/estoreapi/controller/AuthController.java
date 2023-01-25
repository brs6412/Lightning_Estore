package com.estore.api.estoreapi.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.User.Role;
import com.estore.api.estoreapi.payload.AuthRequestData;
import com.estore.api.estoreapi.payload.AuthResponseData;
import com.estore.api.estoreapi.persistence.CartDAO;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.security.jwt.JWT;
import com.estore.api.estoreapi.security.service.UserDetailsInstance;

/**
 * Handles the REST API requests for authenticating users.
 */
@RestController
@RequestMapping("auth")
public class AuthController {
    private static final Logger LOG = Logger.getLogger(AuthController.class.getName());

    @Autowired AuthenticationManager authManager;
    @Autowired UserDAO userDAO;
    @Autowired CartDAO cartDAO;
    @Autowired PasswordEncoder encoder;
    @Autowired JWT jwt;

    /**
     * Responds to the POST request for authenticating a user.
     * @param data User's login credentials
     * @return ResponseEntity with user's {@link AuthResponseData authorization details} and HTTP status of OK if their credentials are successfully verified.
     */
    @PostMapping("login")
    protected ResponseEntity<AuthResponseData> login(@RequestBody AuthRequestData data) {
        LOG.info("POST /auth/login");
        
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwt.generate(auth);
        
        Role role = ((UserDetailsInstance)auth.getPrincipal()).getRole();
        return ResponseEntity.ok(
            new AuthResponseData(data.getUsername(), role, token)
        );
    }

    /**
     * Responds to the POST request for registering a user in the system.
     * @param data User's {@link AuthRequestData registration data}.
     * @return ResponseEntity with user's {@link AuthResponseData authorization details} and HTTP status of CREATED if account successfully created.
     * ResponseEntity with HTTP status of CONFLICT if user already exists in system.
     * ResponseEntity with HTTP status of BAD_REQUEST if validation fails.
     */
    @PostMapping("register")
    protected ResponseEntity<AuthResponseData> register(@RequestBody AuthRequestData data) {
        LOG.info("POST /auth/register");

        if (data.getUsername() == null || data.getUsername().length() < 3 || data.getPassword() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (this.userDAO.getUser(data.getUsername()) != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        
        this.userDAO.createUser(
            data.getUsername(),
            encoder.encode(data.getPassword())
        );

        this.cartDAO.createCart(data.getUsername());

        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwt.generate(auth);
        
        Role role = ((UserDetailsInstance)auth.getPrincipal()).getRole();
        return ResponseEntity.ok(
            new AuthResponseData(data.getUsername(), role, token)
        );
    }
}
