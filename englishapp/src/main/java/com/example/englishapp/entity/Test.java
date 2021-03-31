package com.example.englishapp.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tests")
@Data
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //автоматически генерируется
    private int id;

    @OneToMany(mappedBy = "testS", cascade = CascadeType.ALL)
    private List<Statistic> statistics;

    @OneToMany(mappedBy = "testA", cascade = CascadeType.ALL)
    private List<Answer> answers;

    private int numberOfCorrectAnswers;


}
