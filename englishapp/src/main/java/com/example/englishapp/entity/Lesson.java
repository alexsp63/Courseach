package com.example.englishapp.entity;

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
    private String text;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Statistic> statistics;

    @OneToMany(mappedBy = "lessonId", cascade = CascadeType.ALL)
    private List<Question> questions;


}
