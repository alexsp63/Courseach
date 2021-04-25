package com.example.englishapp.controller;

import lombok.Data;

/**
 * Данные, необходимые для аутентификации
 */
@Data
public class AuthRequest {

    //проверка будет идти по этим полям
    private String login;
    private String password;

    /**
     * Получение логина
     * @return логин
     */
    public String getLogin() {
        return login;
    }

    /**
     * Получение пароля
     * @return пароль
     */
    public String getPassword() {
        return password;
    }
}
