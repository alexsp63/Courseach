package com.example.englishapp.repository;

import com.example.englishapp.entity.AnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerHistoryRepository extends JpaRepository<AnswerHistory, Integer> {
    List<AnswerHistory> findAllByStatistic_Id(Integer statisticsId);
}
