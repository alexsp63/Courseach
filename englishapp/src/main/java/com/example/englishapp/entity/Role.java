package com.example.englishapp.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.QUESTIONS_READ)),
    ADMIN(Set.of(Permission.QUESTIONS_WRITE, Permission.QUESTIONS_READ));

    private final Set<Permission> permissionSet;

    Role(Set<Permission> permissionSet){
        this.permissionSet = permissionSet;
    }

    public Set<Permission> getPermissionSet() {
        return permissionSet;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
