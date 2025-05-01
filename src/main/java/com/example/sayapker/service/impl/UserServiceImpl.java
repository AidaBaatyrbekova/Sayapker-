package com.example.sayapker.service.impl;
import com.example.sayapker.dto.request.*;
import com.example.sayapker.dto.response.AuthResponse;
import com.example.sayapker.dto.response.UserResponse;
import com.example.sayapker.exception.ExceptionMessage;
import com.example.sayapker.exception.InvalidPasswordException;
import com.example.sayapker.mapper.AuthMapper;
import com.example.sayapker.model.entity.Role;
import com.example.sayapker.model.entity.User;
import com.example.sayapker.repository.UserRepository;
import com.example.sayapker.security.jwt.JWTService;
import com.example.sayapker.service.MailService;
import com.example.sayapker.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    // Репозитория жана башка кызматтарды инъекциялоо
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticate;
    AuthMapper authMapper;
    JWTService jwtService;
    MailService mailService;

    /**
     * Колдонуучуну түзүү методу
     * @param request Колдонуучу маалыматтары
     * @return Колдонуучунун жооп форматы
     */
    @Override
    public UserResponse createUser(UserRequest request) {
        // Түзүлгөн User объектиси UserRequest маалыматтарына негизделип түзүлөт
        User user = authMapper.mapToUser(request);

        // Пароль коопсуздугун текшерүү
        if (!isPasswordSecure(request.getPassword())) {
            throw new InvalidPasswordException(ExceptionMessage.PASSWORD_NOT_SECURE);
        }

        // Паролдордун дал келүүсүн текшерүү
        if (!request.getPassword().equals(request.getConfirmThePassword())) {
            throw new InvalidPasswordException(ExceptionMessage.PASSWORD_MISMATCH);
        }

        // Колдонуучунун паролун шифрлөө
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Рольду колдонуучуга коюу (стандарттуу ROLE_USER)
        user.setRole(Role.USER);

        // Колдонуучуну базага сактоо
        userRepository.save(user);

        log.info("Successfully created User " + user.getId()); // Успешно катталган колдонуучу тууралуу маалымат

        // Колдонуучу боюнча жооп кайтаруу
        return authMapper.mapToResponse(user);
    }

    /**
     * Колдонуучуну логин кылуу
     * @param request Логин маалыматтары
     * @return Колдонуучунун логини жана токени
     */
    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            // Колдонуучунун маалыматтарын текшерүү
            authenticate.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(ExceptionMessage.AUTHORIZATION_ERROR); // Эгерде логин/пароль туура эмес болсо
        }

        // Колдонуучуну табуу
        User user = userRepository.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessage.AUTHORIZATION_ERROR));

        // JWT токенин түзүү
        String jwt = jwtService.generateToken(user);

        log.info("Successfully logged in!"); // Логин жасалды

        // Жаңы токен менен жооп кайтаруу
        return AuthResponse.builder()
                .userName(user.getName())
                .role(user.getRole())
                .token(jwt)
                .build();
    }

    /**
     * Колдонуучуга пароль жиберүү үчүн метод
     * @param request Пароль жаңыртуу маалыматы
     * @return Жооп, кат жөнөтүлгөндүгүн билдирүү
     */
    @Override
    @Transactional
    public ResponseEntity<String> resetPassword(PasswordResetRequest request) {
        // Колдонуучу почтасы боюнча издөөгө аракет кылуу
        User user = userRepository.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessage.USER_NOT_FOUND_BY_EMAIL));

        // Жаңы токенди түзүү
        String token = UUID.randomUUID().toString().substring(0, 6); // 6 белгиден турган токен
        user.setResetPasswordToken(token);
        userRepository.save(user); // Токенди колдонуучу менен сактоо

        sendResetPasswordEmail(user.getEmail(), token); // Пароль жиберүү үчүн почта жөнөтүү

        return new ResponseEntity<>("A message has been sent to your email to reset your password!", HttpStatus.OK);
    }

    /**
     * Колдонуучуга түзүлгөн токен менен паролду жаңыртуу
     * @param request Пароль жаңыртуу токенин текшерүү
     * @return Жооп, ийгиликтүү жаңыртылгандыгын билдирүү
     */
    @Override
    @Transactional
    public ResponseEntity<String> resetPasswordToken(PasswordResetTokenRequest request) {
        // Колдонуучуну табуу
        User user = userRepository.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessage.USER_NOT_FOUND_BY_EMAIL));

        // Токенди текшерүү
        if (!user.getResetPasswordToken().equals(request.getToken())) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_RESET_TOKEN);
        }

        // Жаңы пароль текшерүү
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException(ExceptionMessage.PASSWORDS_DONT_MATCH);
        }

        // Пароль коопсуздугун текшерүү
        if (!isPasswordSecure(request.getNewPassword())) {
            throw new IllegalArgumentException(ExceptionMessage.PASSWORD_NOT_SECURE);
        }

        // Парольду жаңыртуу
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null); // Токенди алып салуу
        userRepository.save(user);

        return new ResponseEntity<>("Password successfully updated!", HttpStatus.OK);
    }

    /**
     * Колдонуучунун паролун жаңыртуу
     * @param request Жаңыртуу үчүн азыркы жана жаңы пароль
     * @return Жооп, паролдун ийгиликтүү жаңыртылышы
     */
    @Override
    @Transactional
    public ResponseEntity<String> updatePassword(UpdatePasswordRequest request) {
        // Колдонуучуну табуу
        User user = userRepository.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessage.USER_NOT_FOUND_BY_EMAIL));

        // Азыркы паролду текшерүү
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException(ExceptionMessage.CURRENT_PASSWORD_INCORRECT);
        }

        // Жаңы паролду жана аны тастыктоону текшерүү
        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new InvalidPasswordException(ExceptionMessage.ENTER_NEW_PASSWORD);
        }

        if (request.getNewConfirmPassword() == null || request.getNewConfirmPassword().isEmpty()) {
            throw new InvalidPasswordException(ExceptionMessage.CONFIRM_NEW_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getNewConfirmPassword())) {
            throw new InvalidPasswordException(ExceptionMessage.PASSWORDS_DONT_MATCH);
        }

        // Жаңы паролдун коопсуздугун текшерүү
        if (!isPasswordSecure(request.getNewPassword())) {
            throw new InvalidPasswordException(ExceptionMessage.PASSWORD_NOT_SECURE);
        }

        // Жаңы паролду жаңыртуу
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ResponseEntity<>("Password successfully updated!", HttpStatus.OK);
    }

    /**
     * Пароль коопсуздугун текшерүү методу
     * @param newPassword Жаңы пароль
     * @return true - коопсуздугу жогору, false - төмөн
     */
    public boolean isPasswordSecure(String newPassword) {
        if (newPassword.length() < 8) return false; // 8 символдон кем болбошу керек

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // Парольдун мүнөздөмөлөрүн текшерүү
        for (char ch : newPassword.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpperCase = true;
            else if (Character.isLowerCase(ch)) hasLowerCase = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else hasSpecialChar = true;
        }

        // Бардык талаптарга жооп берген болсо, true кайтаруу
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }

    /**
     * Пароль жаңыртууну колдонуучуга билдирүү үчүн почта жөнөтүү
     * @param email Колдонуучунун почтасы
     * @param token Пароль жаңыртуу токени
     */
    private void sendResetPasswordEmail(String email, String token) {
        String subject = "Password Reset Request";
        String message = "Your password reset code is: " + token;
        mailService.sendSimpleMessage(email, subject, message);
    }
}