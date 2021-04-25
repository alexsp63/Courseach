package com.example.englishapp.config;

import com.example.englishapp.security.JWTTokenFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Конфигурация токена
 */
@Component
public class JWTConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JWTTokenFilter jwtTokenFilter;

    /**
     * Конструктор
     * @param jwtTokenFilter - экземпляр созданного класса
     */
    public JWTConfig(JWTTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }


    /**
     * Добавление в начало нашего фильтра токена
     * @param httpSecurity - из переопределния
     * @throws Exception исключение
     */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
