package com.example.englishapp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "k_statistics")
@Data
@NoArgsConstructor
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
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
    private List<AnswerHistory> answerHistories;


}
