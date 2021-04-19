package com.example.englishapp.service;

import com.example.englishapp.entity.AnswerHistory;
import com.example.englishapp.repository.AnswerHistoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerHistoryService {

    @Autowired
    private AnswerHistoryRepository answerHistoryRepository;


    public List<AnswerHistory> findAll(Integer statisticsId){
        if (statisticsId != null){
            return answerHistoryRepository.findAllByStatistic_Id(statisticsId);
        }
        return answerHistoryRepository.findAll();
    }

    public AnswerHistory create(AnswerHistory answerHistory){
        return answerHistoryRepository.save(answerHistory);
    }

    public AnswerHistory update(AnswerHistory answerHistory, AnswerHistory answerHistoryFromDB) {
        BeanUtils.copyProperties(answerHistory, answerHistoryFromDB, "id");
        return answerHistoryRepository.save(answerHistoryFromDB);
    }

    public boolean delete(AnswerHistory answerHistory) {
        if (answerHistory != null){
            answerHistoryRepository.delete(answerHistory);
            return true;
        }
        return false;
    }
}
