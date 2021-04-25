package com.example.englishapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Класс кофигурации security
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTConfig jwtConfig;

    /**
     * Конструктор
     * @param jwtConfig - экземпляр созданного JWTConfig
     */
    public SecurityConfig(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Кастомная конфигурация
     * здесь отключаем сессию, указываем доступ,
     * в корень проекта доступ имеют все (у меня там всё равно пусто, так что вернётся 404),
     * на авторизацию доступ также есть у всех,
     * POST на таблицу пользователей тоже разрешён всем (так как это регистрация),
     * получать уже занятые логины тоже могут все, это нужно для регистрации,
     * вместо стандартной секьюрной странички применяю созданные конфигурации
     * @param http - так как использую http security
     * @throws Exception исключение
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.POST, "/user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/logins").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .apply(jwtConfig);
    }

    /**
     * Для хеширования паролей с силой 12
     * @return кодировщик паролей
     */
    @Bean
    protected PasswordEncoder passwordEncoder(){
        //не хранить же пароли в виде строки
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Переопределние бина менеджера аутентификации
     * @return дефолтное
     * @throws Exception исключение
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
