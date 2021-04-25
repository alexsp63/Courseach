package com.example.englishapp.controller;

import com.example.englishapp.entity.User;
import com.example.englishapp.repository.UserRepository;
import com.example.englishapp.security.JWTTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер, отвечающий за авторизацию
 */
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;

    /**
     * Конструктор
     * @param authenticationManager - секьюрный менеджер аутентификации
     * @param userRepository - созданный пользовательский репозиторий
     * @param jwtTokenProvider - созданный компонент token provider
     */
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Обработка запросов на аутентификацию
     * @param request - данные, с которыми человек хочет авторизоваться (логин и пароль)
     * @return словарь данных пользователя и созданный ему токен на основе его логина, роли и времени,
     * или же сущность ответа, имеющую в виде тела конкретное сообщение об ошибке (неверные данные или аккаунт заблокирован)
     * и хороший статус ответа (чтобы мой клиент мог прочитать переданное сообщение об ошибке)
     */
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
            response.put("password", password);
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("role", user.getRole().name());
            response.put("status", user.getStatus().name());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK); //всё равно токена не возвращается
        }
    }
}
