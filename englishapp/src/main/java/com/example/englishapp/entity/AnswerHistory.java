package com.example.englishapp.entity;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "k_answer_history")
@Data
@NamedEntityGraph
public class AnswerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_login")
    @NotNull
    private User userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "statistics_id")
    @NotNull
    private Statistic statistic;

}
