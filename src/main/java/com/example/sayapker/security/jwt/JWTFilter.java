package com.example.sayapker.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.sayapker.model.entity.User;
import com.example.sayapker.repository.UserRepository;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public JWTFilter(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                if (StringUtils.hasText(token)) {
                    String email = jwtService.extractEmail(token);
                    User user = userRepository.getUserByEmail(email)
                            .orElseThrow(ChangeSetPersister.NotFoundException::new);
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    null,
                                    user.getAuthorities()
                            )
                    );
                }
            } catch (JWTVerificationException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            } catch (ChangeSetPersister.NotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}