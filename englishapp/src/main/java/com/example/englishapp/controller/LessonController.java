package com.example.englishapp.controller;

import com.example.englishapp.entity.Lesson;
import com.example.englishapp.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {

    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<List<Lesson>> readAll(){
        final List<Lesson> lessonList = lessonService.findAll();
        return lessonList != null && !lessonList.isEmpty()
                ? new ResponseEntity<>(lessonList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getOne(@PathVariable(name = "id") Lesson lesson){
        final Lesson thisLesson = lesson;
        return thisLesson != null
                ? new ResponseEntity<>(thisLesson, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Lesson lesson){
        Lesson newAnswer = lessonService.create(lesson);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Lesson lessonFromDB,
                                    @RequestBody Lesson lesson)
    {
        Lesson updatedLesson = lessonService.update(lesson, lessonFromDB);
        if (updatedLesson != null) {
            return new ResponseEntity<>(updatedLesson, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Lesson>> delete(@PathVariable("id") Lesson lesson) {
        if (lessonService.delete(lesson)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
