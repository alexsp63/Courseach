package com.example.englishapp.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Перечисление ролей
 */
public enum Role {
    USER(Set.of(Permission.TABLE_READ)),
    ADMIN(Set.of(Permission.TABLE_READ, Permission.TABLE_WRITE));

    private final Set<Permission> permissionSet;

    /**
     * Конструктор
     * @param permissionSet - разрешения для роли
     */
    Role(Set<Permission> permissionSet){
        this.permissionSet = permissionSet;
    }

    /**
     * Получение разрешений для роли
     * @return
     */
    public Set<Permission> getPermissionSet() {
        return permissionSet;
    }

    /**
     * Получаем множество SimpleGrantedAuthority, кто и к чему имеет доступ
     * @return конвертированные в SimpleGrantedAuthorities кастомные роли с их правами
     */
    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
