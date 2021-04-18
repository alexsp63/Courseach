package com.example.englishapp.controller;

import com.example.englishapp.entity.Lesson;
import com.example.englishapp.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('table:read')")
    public ResponseEntity<List<Lesson>> readAll(
            @RequestParam(required=false) Integer questionId
    ){
        final List<Lesson> lessonList = lessonService.findAll(questionId);
        return lessonList != null && !lessonList.isEmpty()
                ? new ResponseEntity<>(lessonList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('table:read')")
    public ResponseEntity<Lesson> getOne(@PathVariable(name = "id") Lesson lesson){
        final Lesson thisLesson = lesson;
        return thisLesson != null
                ? new ResponseEntity<>(thisLesson, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('table:write')")
    public ResponseEntity<?> create(@RequestBody Lesson lesson){
        Lesson newAnswer = lessonService.create(lesson);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('table:write')")
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
    @PreAuthorize("hasAuthority('table:write')")
    public ResponseEntity<List<Lesson>> delete(@PathVariable("id") Lesson lesson) {
        if (lessonService.delete(lesson)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
