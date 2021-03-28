package com.example.englishapp.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    private String login;

    private String password;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Question> questions;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", questions=" + questions +
                '}';
    }
}
