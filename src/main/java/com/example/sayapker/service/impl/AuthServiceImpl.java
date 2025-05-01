package com.example.sayapker.service.impl;

import com.example.sayapker.dto.request.AuthRequest;
import com.example.sayapker.dto.request.UserRegisterRequest;
import com.example.sayapker.dto.response.AuthResponse;
import com.example.sayapker.exception.UserNotFoundException;
import com.example.sayapker.model.entity.User;
import com.example.sayapker.repository.UserRepository;
import com.example.sayapker.security.jwt.JWTService;
import com.example.sayapker.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    // Колдонуучу репозитори жана башка кызматтарды инъекциялоо
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JWTService jwtService;
    AuthenticationManager authenticationManager;

    // Колдонуучуну каттоо үчүн метод
    @Override
    public AuthResponse register(UserRegisterRequest request) {
        // Колдонуучуну түзүү
        User user = User.builder()
                .name(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))  // Колдонуучунун паролун шифрлөө
                .role(request.getRole())
                .build();

        // Колдонуучуну базага сактоо
        userRepository.save(user);

        // JWT токенин түзүү
//String token = jwtService.generateToken(user.getEmail());

        // Жаңы түзүлгөн колдонуучу менен токенди кайтаруу
        return new AuthResponse(user.getName(), user.getRole(), jwtService.generateToken(user));
    }

    // Колдонуучуну киргизүү (логин) үчүн метод
    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            // Колдонуучу кирген маалыматтардын туура экенин текшерүү
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            // Колдонуучуну репозиториядан алуу
            User user = userRepository.getUserByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Колдонуучу жана токенди жооп катары кайтаруу
            return new AuthResponse(user.getName(), user.getRole(), jwtService.generateToken(user));

        } catch (BadCredentialsException e) {
            // Эгерде логин/пароль туура эмес болсо
            throw new RuntimeException("Invalid username or password", e);
        }
    }
}