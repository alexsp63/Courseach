package com.example.englishapp.service;

import com.example.englishapp.entity.Lesson;
import com.example.englishapp.repository.LessonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;


    public List<Lesson> findAll(){
        return lessonRepository.findAll();
    }

    public Lesson create(Lesson lesson){
        return lessonRepository.save(lesson);
    }

    public Lesson update(Lesson lesson, Lesson lessonFromDB) {
        BeanUtils.copyProperties(lesson, lessonFromDB, "id");
        return lessonRepository.save(lessonFromDB);
    }

    public boolean delete(Lesson lesson) {
        if (lesson != null){
            lessonRepository.delete(lesson);
            return true;
        }
        return false;
    }
}
