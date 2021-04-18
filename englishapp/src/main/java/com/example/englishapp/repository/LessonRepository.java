package com.example.englishapp.repository;

import com.example.englishapp.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByQuestions_Id(Integer id);
}
