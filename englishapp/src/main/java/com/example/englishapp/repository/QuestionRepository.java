package com.example.englishapp.repository;

import com.example.englishapp.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * интерфейс, позволяющий взаимодействовать с табдлицей вопросов
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    /**
     * поиск всех вопросов по конкретному уроку
     * @param id - уникальный идентификатор урока
     * @return список вопросов, добавленных в этот урок
     */
    List<Question> findAllByLessonId_Id(Integer id);
}
