package com.example.englishapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


/**
 * Отражает сущность пользователя с атрибутами:
 * уникальный логин,
 * пароль (хранится в захешированном виде для безопасности),
 * имя,
 * фамилию,
 * роль (пользователь или администратор),
 * статус (только активный статус позволяет войти пользователю в ситему)
 */
@Entity
@Table(name = "k_user")
@Data
@NoArgsConstructor
public class User {

    @Id
    private String login;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference(value = "user_statistics")
    private List<Statistic> statistics;

    /**
     * получение логина пользователя
     * @return логин
     */
    public String getLogin() {
        return login;
    }

    /**
     * получение фамилии пользователя
     * @return фамилию пользователя
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * получение имени пользователя
     * @return имя пользователя
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * установка пользователю пароля
     * @param password - новый пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * получение пароля пользователя
     * @return пароль
     */
    public String getPassword() {
        return password;
    }

    /**
     * установка пользователю определённой роли
     * @param role - роль, которую надо установить пользователя
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * получение роли пользователя
     * @return роль пользователя
     */
    public Role getRole() {
        return role;
    }

    /**
     * получение статуса пользователя
     * @return статус пользователя
     */
    public Status getStatus() {
        return status;
    }
}

