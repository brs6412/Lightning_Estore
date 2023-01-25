package com.estore.api.estoreapi.security.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.User.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * User details object to be used with authentication context.
 */
public class UserDetailsInstance implements UserDetails {
    private int id;
    private String username;
    @JsonIgnore private String password;
    private Role role = Role.CUSTOMER;
    @JsonIgnore private Collection<? extends GrantedAuthority> authorities;

    /**
     * Builds a UserDetailsInstance from a User entity.
     * @param user User to build details from.
     * @return Constructed UserDetailsInstance.
     */
    public static UserDetailsInstance build(User user) {
        UserDetailsInstance details = new UserDetailsInstance();

        details.id = user.getId();
        details.username = user.getUsername();
        details.password = user.getPassword();
        details.role = user.getRole();
        details.authorities = Arrays.asList(new SimpleGrantedAuthority(details.role.name()));

        return details;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return this.authorities;  }

    @Override public String getPassword() { return this.password; }
    @Override public String getUsername() { return this.username; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override public boolean isEnabled() { return true; }

    public int getId() { return this.id; }
    public Role getRole() { return this.role; }
}
