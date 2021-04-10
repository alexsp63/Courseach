package com.example.englishapp.repository;

import com.example.englishapp.entity.AnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerHistoryRepository extends JpaRepository<AnswerHistory, Integer> {
}
