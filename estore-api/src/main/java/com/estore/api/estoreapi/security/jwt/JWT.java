package com.estore.api.estoreapi.security.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.security.service.UserDetailsInstance;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Main component for signing/verifying/reading JWT authorization data.
 */
@Component
public class JWT {
    private SecretKey key;

    public JWT(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Generates a JWT from authentication data.
     * @param authentication User authentication details
     * @return Signed JWT string
     */
    public String generate(Authentication authentication) {
        UserDetailsInstance principal = (UserDetailsInstance) authentication.getPrincipal();

        return Jwts.builder()
            .setSubject(principal.getUsername())
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(8, ChronoUnit.HOURS)))
            .signWith(this.key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Verifies and returns username of given JWT.
     * @param token Authorization JWT.
     * @return Username if token is verified, null otherwise.
     */
    public String getUsername(String token) {
        try {
            // If no exception is thrown, the token can be trusted.
            return Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        } catch (Exception ex) { return null; }
    }
    
}
