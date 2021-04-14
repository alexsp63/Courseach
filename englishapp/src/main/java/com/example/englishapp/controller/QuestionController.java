package com.example.englishapp.controller;

import com.example.englishapp.entity.Question;
import com.example.englishapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('table:read')")
    public ResponseEntity<List<Question>> getAll(
            @RequestParam(required=false) Integer lessonId
    ){
        final List<Question> questionList = questionService.findAll(lessonId);
        return questionList != null && !questionList.isEmpty()
                ? new ResponseEntity<>(questionList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('table:read')")
    public ResponseEntity<Question> getOne(@PathVariable(name = "id") Question question){
        final Question thisQuestion = question;
        return thisQuestion != null
                ? new ResponseEntity<>(thisQuestion, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('table:write')")
    public ResponseEntity<?> create(@RequestBody Question question){
        Question newAnswer = questionService.create(question);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('table:write')")
    public ResponseEntity<?> update(@PathVariable(name = "id") Question questionFromDB,
                                    @RequestBody Question question)
    {
        Question updatedQuestion = questionService.update(question, questionFromDB);
        if (updatedQuestion != null) {
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('table:write')")
    public ResponseEntity<List<Question>> delete(@PathVariable("id") Question question) {
        if (questionService.delete(question)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
