package com.example.englishapp.repository;

import com.example.englishapp.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * репозиторий, позволяющий работать с таблицей уроков
 */
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    /**
     * получение урока, к которому относится передаваемый вопрос
     * @param id - уникальный идентификатор вопроса
     * @return урок, в котором записан передаваемый вопрос
     */
    List<Lesson> findByQuestions_Id(Integer id);
}
