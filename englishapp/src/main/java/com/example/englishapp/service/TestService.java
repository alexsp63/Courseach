package com.example.englishapp.service;

import com.example.englishapp.entity.Test;
import com.example.englishapp.repository.TestRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {


    @Autowired //автоматич создает данный объект
    private TestRepository testRepository;


    public List<Test> findAll(){
        return testRepository.findAll();
    }


    public Test create(Test test){
        return testRepository.save(test);
    }

    public Test update(Test test, Test testFromDB) {
        BeanUtils.copyProperties(test, testFromDB, "id");
        return testRepository.save(testFromDB);
    }

    public boolean delete(Test test) {
        if (test != null){
            testRepository.delete(test);
            return true;
        }
        return false;
    }
}
