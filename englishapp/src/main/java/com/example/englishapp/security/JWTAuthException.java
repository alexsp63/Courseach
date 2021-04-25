package com.example.englishapp.security;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.naming.AuthenticationException;

/**
 * Собственное исключение для невалидного токена
 */
@Getter
public class JWTAuthException extends AuthenticationException {

    private HttpStatus httpStatus;

    /**
     * Конструктор
     * @param msg - передаваемое мной сообщение
     */
    public JWTAuthException(String msg) {
        super(msg);
    }

    /**
     * Ещё один конструктор
     * @param msg - передаваемое сообщение
     * @param httpStatus - статус, который необходимо установить
     */
    public JWTAuthException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    /**
     * Получение статуса
     * @return установленный http статус
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
