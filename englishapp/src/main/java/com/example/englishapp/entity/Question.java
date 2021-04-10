package com.example.englishapp.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "k_question")
@Data
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String text;

    @NotNull
    private String correctAnswer;

    @NotNull
    private String description;

    private String incorrect1;

    private String incorrect2;

    private String incorrect3;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    @NotNull
    private Lesson lessonId;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswerHistory> answerHistories;

}
