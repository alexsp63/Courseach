package com.example.englishapp.entity;

public enum Permission {

    QUESTIONS_READ("questions:read"),
    QUESTIONS_WRITE("questions:write");

    private final String permission;

    Permission(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
