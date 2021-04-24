package com.example.englishapp.repository;

import com.example.englishapp.entity.AnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * интерфейс, позволяющий работать с таблицей истории ответов
 */
public interface AnswerHistoryRepository extends JpaRepository<AnswerHistory, Integer> {
    /**
     * получение истории ответов, принадлежащей переданной статистике
     * @param statisticsId - уникальный идентификатор статистики, для которой надо искать историю ответов
     * @return список объектов истории ответов, которые принадлежат передаваемой статистике
     */
    List<AnswerHistory> findAllByStatistic_Id(Integer statisticsId);
}
