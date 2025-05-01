package com.example.sayapker.controller;
import com.example.sayapker.dto.request.AuthRequest;
import com.example.sayapker.dto.request.UserRegisterRequest;
import com.example.sayapker.dto.response.AuthResponse;
import com.example.sayapker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")  // Бүтүндөй маршрут үчүн "/auth" жолу колдонулат
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;  // AuthService инъекциясы

    // Колдонуучу каттоо үчүн POST метод
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)  // Жаңы колдонуучу катталганын билдирүү үчүн CREATED статусу
    public AuthResponse register(@RequestBody UserRegisterRequest request) {
        return authService.register(request);  // Register сервисин чакыруу
    }

    // Колдонуучу кирүү (логин) үчүн POST метод
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);  // Login сервисин чакыруу
    }
}