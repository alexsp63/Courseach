package com.example.englishapp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр пропускает через себя запросы от клиента
 */
@Component
public class JWTTokenFilter extends GenericFilter {

    private final JWTTokenProvider jwtTokenProvider;

    /**
     * Конструктор
     * @param jwtTokenProvider - экземпляр созданного класса
     */
    public JWTTokenFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Фильтрвция токена
     * @param servletRequest - запрос из переопределния
     * @param servletResponse - ответ
     * @param filterChain - фильтрация из переопределения
     * @throws IOException исключение
     * @throws ServletException исключение
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                //если токен валидный, получаю аутентификацию
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JWTAuthException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) servletResponse).sendError(e.getHttpStatus().value());
            try {
                throw new JWTAuthException("Невалидный токен");
            } catch (JWTAuthException jwtAuthException) {
                jwtAuthException.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
