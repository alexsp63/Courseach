package com.example.englishapp.controller;

import com.example.englishapp.entity.Answer;
import com.example.englishapp.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping
    public ResponseEntity<List<Answer>> readAll(){
        final List<Answer> answerList = answerService.findAll();
        return answerList != null && !answerList.isEmpty()
                ? new ResponseEntity<>(answerList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //получение значения по login
    @GetMapping("/{id}")
    public ResponseEntity<Answer> getOne(@PathVariable(name = "id") Answer answer){
        final Answer thisAnswer = answer;
        return thisAnswer != null
                ? new ResponseEntity<>(thisAnswer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //добавление записи
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Answer answer){
        Answer newAnswer = answerService.create(answer);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    //обновление конкретной записи, которая уже существует
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Answer answerFromDB, //answer из бд
                                    @RequestBody Answer answer) //answer от пользователя
    {
        Answer updatedAnswer = answerService.update(answer, answerFromDB);
        if (updatedAnswer != null) {
            return new ResponseEntity<>(updatedAnswer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Answer>> delete(@PathVariable("id") Answer answer) {
        if (answerService.delete(answer)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
