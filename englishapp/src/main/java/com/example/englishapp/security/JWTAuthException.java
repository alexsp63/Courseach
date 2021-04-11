package com.example.englishapp.security;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.naming.AuthenticationException;

@Getter
public class JWTAuthException extends AuthenticationException {
    private HttpStatus httpStatus;

    public JWTAuthException(String msg) {
        super(msg);
    }
    public JWTAuthException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
