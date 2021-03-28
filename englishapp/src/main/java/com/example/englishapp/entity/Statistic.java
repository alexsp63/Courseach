package com.example.englishapp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "statistics")
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //автоматически генерируется
    private int id;

    private int mark;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_login")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private Test testS;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public Test getTestS() {
        return testS;
    }

    public void setTestS(Test testS) {
        this.testS = testS;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                ", mark=" + mark +
                ", date=" + date +
                ", user=" + user +
                ", testS=" + testS +
                '}';
    }
}
