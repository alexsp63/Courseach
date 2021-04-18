package com.example.englishapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "statistics_id")
    @NotNull
    private Statistic statistic;

    public boolean isCorrect() {
        return isCorrect;
    }
}
