package com.example.englishapp.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

/**
 * Создание токена для пользователя
 */
@Component
public class JWTTokenProvider {

    private final UserDetailsService userDetailsService;
    @Value("${jwt.secret}")
    private String key;
    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${jwt.expiration}")
    private long millisecs;

    /**
     * Конструктор
     * @param userDetailsService - для получения аутентификации пользователя
     */
    public JWTTokenProvider(@Qualifier("userDetailsServiceImp") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Для безопасности шифрую в строку секретный ключ
     */
    @PostConstruct
    protected void init() {
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    /**
     * Создание токена на основе передаваемых данных
     * @param login - передаваемый логин
     * @param role - роль пользователя
     * @return строку токена на основе логина, роли, даты создания и даты истечения,
     * подписанный с помощью алгоритма HS256 и секретного ключа
     */
    public String createToken(String login, String role){
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + millisecs * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    /**
     * Валидация токена на корректность
     * @param token - сам токен, который надо провалидировать
     * @return валидный ли токен, не истёк ли его срок действия
     * @throws JWTAuthException - саомосозданное исключение
     */
    public boolean validateToken(String token) throws JWTAuthException {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JWTAuthException("Невалидный токен", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Получение аутентификации пользователя
     * @param token - токен
     * @return
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getLogin(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Получение логина пользователя из токена с помощью секретного ключа
     * @param token - токен, созданный для этого пользователя
     * @return полученный логин
     */
    public String getLogin(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Получение токена из сделанного пользователем запроса с заголовком Authorization
     * @param request - запрос
     * @return токен по заголовку "Authorization"
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }
}
