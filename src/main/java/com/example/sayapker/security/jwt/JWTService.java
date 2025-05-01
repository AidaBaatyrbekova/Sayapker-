package com.example.sayapker.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.sayapker.model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTService {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private static final long TOKEN_EXPIRATION_DAYS = 7L; // 7 күнгө жарактуу

    // Токенди жаратуу
    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(ZonedDateTime.now().plusDays(TOKEN_EXPIRATION_DAYS).toInstant()))
                .sign(Algorithm.HMAC256(secretKey));
    }

    // Токенди текшерүү жана email алуу
    public String extractEmail(String token) {
        return getDecodedJWT(token).getSubject();
    }

    // Токенден роль алуу
    public String extractRole(String token) {
        return getDecodedJWT(token).getClaim("role").asString();
    }

    // Токендин мөөнөтү өтүп кеткенин текшерүү
    public boolean isTokenExpired(String token) {
        return getDecodedJWT(token).getExpiresAt().before(new Date());
    }

    // Токендин тууралыгын текшерүү
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Токенди текшерип DecodedJWT алуу
    private DecodedJWT getDecodedJWT(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            return verifier.verify(token);
        } catch (TokenExpiredException e) {
            throw new RuntimeException("Токен мөөнөтү бүттү. Кайра кирип чыгыңыз.", e);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Токен туура эмес!", e);
        }
    }
}