package com.example.englishapp.service;

import com.example.englishapp.entity.Question;
import com.example.englishapp.repository.QuestionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;


    public List<Question> findAll(Integer lessonId){
        if (lessonId != null){
            return questionRepository.findAllByLessonId_Id(lessonId);
        }
        return questionRepository.findAll();
    }


    public Question create(Question question){
        return questionRepository.save(question);
    }

    public Question update(Question question, Question questionFromDB) {
        BeanUtils.copyProperties(question, questionFromDB, "id");
        return questionRepository.save(questionFromDB);
    }

    public boolean delete(Question question) {
        if (question != null){
            questionRepository.delete(question);
            return true;
        }
        return false;
    }
}
