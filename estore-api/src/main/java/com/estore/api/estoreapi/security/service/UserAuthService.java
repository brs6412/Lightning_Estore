package com.estore.api.estoreapi.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserDAO;

/**
 * Service used to help AuthenticationManager retrieve users
 * to verify user authorization.
 */
@Service
public class UserAuthService implements UserDetailsService {
    @Autowired private UserDAO dao;

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.dao.getUser(username);
        if (user == null)
            throw new UsernameNotFoundException("Couldn't find user with name: " + username);
        return UserDetailsInstance.build(user);
    }
}
