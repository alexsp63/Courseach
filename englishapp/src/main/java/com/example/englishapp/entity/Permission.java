package com.example.englishapp.entity;


public enum Permission {

    TABLE_READ("table:read"),
    TABLE_WRITE("table:write");

    private final String permission;

    Permission(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
