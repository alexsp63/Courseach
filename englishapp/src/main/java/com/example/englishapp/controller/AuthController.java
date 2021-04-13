package com.example.englishapp.controller;

import com.example.englishapp.entity.User;
import com.example.englishapp.repository.UserRepository;
import com.example.englishapp.security.JWTTokenProvider;
import com.example.englishapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {
        try {
            String login = request.getLogin();
            String password = request.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException("Такого пользователя не существует"));
            String token = jwtTokenProvider.createToken(login, user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("login", login);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            System.out.println(e);
            return new ResponseEntity<>("Неверные логин или пароль!", HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
