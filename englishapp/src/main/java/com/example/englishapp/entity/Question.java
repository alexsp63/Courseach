package com.example.englishapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Этот класс отражает сущность вопроса в базе данных,
 * имеет автоматически генерируемый уникальный идентификатор,
 * текст вопроса,
 * обязательно имеет правильный ответ,
 * описание вопроса (небольшое пояснение правильного ответа),
 * может иметь один неправильный ответ (если нужно выбрать правильный ответ из двух вариантов)
 * и ещё может быть ещё 2 неправильных ответа (если вопрос с четырьмя вариантами ответа, и из них надо выбрать один правильный)
 */
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
    @JsonBackReference(value = "question_answerHistories")
    private List<AnswerHistory> answerHistories;

}
