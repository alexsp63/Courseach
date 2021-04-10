package com.example.englishapp.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    private List<Statistic> statistics;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<AnswerHistory> answerHistories;

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

