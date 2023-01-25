package com.estore.api.estoreapi.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.security.service.UserDetailsInstance;

/**
 * HTTP security filter layer to set request authorization context.
 */
public class TokenFilter extends OncePerRequestFilter {
    @Autowired private JWT jwt;    
    @Autowired private UserDAO dao;
    
    /**
     * Verifies that the request has a valid JWT attached to the Authorization header
     * and sets the authorization context.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
        String token = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken authentication = null;
        if (token != null && !token.isEmpty()) {

            if (token.startsWith("Bearer "))
                token = token.substring(7, token.length());
            
            String username = jwt.getUsername(token);
            if (username != null) {
                UserDetailsInstance details = UserDetailsInstance.build(this.dao.getUser(username));
                authentication =
                    new UsernamePasswordAuthenticationToken(
                        details,
                        null,
                        details.getAuthorities()
                    );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
