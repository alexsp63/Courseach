package com.example.englishapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //автоматически генерируется
    private int id;

    private String text;
    private String correctAnswer;
    private String incorrect1;
    private String incorrect2;
    private String incorrect3;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_login")
    private User admin;

    @OneToMany(mappedBy = "testA", cascade = CascadeType.ALL)
    private List<Answer> answers;

}
