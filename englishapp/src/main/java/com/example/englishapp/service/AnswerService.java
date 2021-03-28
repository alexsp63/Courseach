package com.example.englishapp.service;

import com.example.englishapp.entity.Answer;
import com.example.englishapp.entity.User;
import com.example.englishapp.repository.AnswerRepository;
import com.example.englishapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    @Autowired //автоматич создает данный объект
    private AnswerRepository answerRepository;


    public List<Answer> findAll(){
        return answerRepository.findAll();
    }


    public Answer create(Answer answer){
        return answerRepository.save(answer);
    }

    public Answer update(Answer Answer, Answer AnswerFromDB) {
        BeanUtils.copyProperties(Answer, AnswerFromDB, "id");
        return answerRepository.save(AnswerFromDB);
    }

    public boolean delete(Answer Answer) {
        if (Answer != null){
            answerRepository.delete(Answer);
            return true;
        }
        return false;
    }

}
