package com.example.englishapp.security;

import com.example.englishapp.entity.Status;
import com.example.englishapp.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Класс для приведения сущности пользователя из моей базы данных к тому виду,
 * который будет понимать Spring Security - здесь важны его логин и пароль (для аутентификации)
 * и роль со статусом (для авторизации)
 */
@Data
public class SecurityUser implements UserDetails {

    private final String login;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    /**
     * Конструктор
     * @param login - логин пользователя
     * @param password - пароль пользователя
     * @param authorities - коллекция прав пользователя
     * @param isActive - активен ли пользователь, разрешён ли ему вход в систему
     */
    public SecurityUser(String login, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.login = login;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    /**
     * Получение прав пользователей
     * @return права пользователя
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Получение пароля пользователя
     * @return пароль пользователя
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Получение логина пользователя
     * @return логин пользователя
     */
    @Override
    public String getUsername() {
        return login;
    }

    /**
     * Проверка срока действия аккаунта
     * @return true или false в зависимости от статуса
     */
    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    /**
     * Не заблокирован ли аккаунт
     * @return true, если аккаунт активен, false, если заблокирован
     */
    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    /**
     * Не истек ли срок действия учётных данных
     * @return true, если аккаунт активен, иначе false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    /**
     * Доступен ли аккаунт
     * @return true, если статус ACTIVE, иначе false
     */
    @Override
    public boolean isEnabled() {
        return isActive;
    }

    /**
     * Конвертация пользователя из базы данных в пользователя, понятному Security,
     * с его ролью и статусом
     * @param user - ORM'ка пользователя
     * @return секьюрный пользователь
     */
    public static UserDetails fromUser(User user){
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getRole().getAuthorities()
        );
    }
}
