package com.example.englishapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Этот класс отражает сущность статистики,
 * имеет автоматически генерируемый уникальный идентификатор,
 * дату создания объекта (то есть дату прохождения пользователем теста),
 * количество правильных ответов, данных пользователем,
 * внешний ключ - пользователя, для которого ведётся учёт прохождения теста,
 * внешний ключ - урок, по которому формируется это статистика,
 * список историй ответов, которые созданы по этой статистике
 */
@Entity
@Table(name = "k_statistics")
@Data
@NoArgsConstructor
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;

    @NotNull
    private int score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_login")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    @NotNull
    private Lesson lesson;

    @OneToMany(mappedBy = "statistic", cascade = CascadeType.ALL)
    @JsonBackReference(value = "answerHistories_statistics")
    private List<AnswerHistory> answerHistories;


}
