package com.example.englishapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "k_lesson")
@NoArgsConstructor
@Data
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String text;

    @NotNull
    private String questionType;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @JsonBackReference(value = "statistics_lesson")
    private List<Statistic> statistics;

    @OneToMany(mappedBy = "lessonId", cascade = CascadeType.ALL)
    @JsonBackReference(value = "question_lesson")
    private List<Question> questions;


}
