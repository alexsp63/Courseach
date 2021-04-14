package com.example.englishapp.repository;

import com.example.englishapp.entity.Question;
import com.example.englishapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findAllByLessonId_Id(Integer id);
}
