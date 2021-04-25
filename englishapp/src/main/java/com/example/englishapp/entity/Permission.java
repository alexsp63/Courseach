package com.example.englishapp.entity;


/**
 * Перечисление разрешений, отображение их в проекте
 */
public enum Permission {

    TABLE_READ("table:read"),
    TABLE_WRITE("table:write");

    private final String permission;

    /**
     * Конструктор
     * @param permission - разрешение
     */
    Permission(String permission){
        this.permission = permission;
    }

    /**
     * получение разрешения
     * @return одно из двух возможных
     */
    public String getPermission() {
        return permission;
    }
}
