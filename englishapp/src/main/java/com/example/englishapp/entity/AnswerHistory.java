package com.example.englishapp.entity;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

/**
 *Сущность истории ответов с атрибутами:
 * автоматически генерируемый уникальный идентификатор,
 * булевая правильность ответа пользователя на вопрос,
 * внешний ключ - вопрос, на который и был дан ответ,
 * внешний ключ - пользователь, который дал этот ответ.
 * Служит для хранения истории правильности данных ответов
 */
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
}
