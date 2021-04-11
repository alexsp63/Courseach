package com.example.englishapp.controller;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
