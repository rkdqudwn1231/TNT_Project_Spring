

package com.tnt.project.utils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private long expiration;
    
    public String createToken(String id, List<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(id)
                .withClaim("roles",roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(algorithm);
    }
    
    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
    
    public String getIdFromToken(String token) {
       DecodedJWT data = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
       return data.getSubject();
    }
    
    public List<String> getRolesFromToken(String token) {
       DecodedJWT data = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
       return data.getClaim("roles").asList(String.class);
    }

}











