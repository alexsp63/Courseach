package com.example.englishapp.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //автоматически генерируется
    private int id;

    private String text;
    private String correctAnswer;
    private String incorrect1;
    private String incorrect2;
    private String incorrect3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_login")
    private Admin admin;

    @OneToMany(mappedBy = "testA", cascade = CascadeType.ALL)
    private List<Answer> answers;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getIncorrect1() {
        return incorrect1;
    }

    public String getIncorrect2() {
        return incorrect2;
    }

    public String getIncorrect3() {
        return incorrect3;
    }

    public void setIncorrect1(String incorrect1) {
        this.incorrect1 = incorrect1;
    }

    public void setIncorrect2(String incorrect2) {
        this.incorrect2 = incorrect2;
    }

    public void setIncorrect3(String incorrect3) {
        this.incorrect3 = incorrect3;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrect1='" + incorrect1 + '\'' +
                ", incorrect2='" + incorrect2 + '\'' +
                ", incorrect3='" + incorrect3 + '\'' +
                ", admin=" + admin +
                ", answers=" + answers +
                '}';
    }
}
