package com.example.englishapp.controller;

import com.example.englishapp.entity.AnswerHistory;
import com.example.englishapp.service.AnswerHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer_history")
public class AnswerHistoryController {

    private final AnswerHistoryService answerHistoryService;

    @Autowired
    public AnswerHistoryController(AnswerHistoryService answerHistoryService) {
        this.answerHistoryService = answerHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<AnswerHistory>> readAll(
            @RequestParam(required=false) Integer statisticsId
    ){
        final List<AnswerHistory> answerHistoryList = answerHistoryService.findAll(statisticsId);
        return answerHistoryList != null && !answerHistoryList.isEmpty()
                ? new ResponseEntity<>(answerHistoryList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerHistory> getOne(@PathVariable(name = "id") AnswerHistory answerHistory){
        final AnswerHistory thisAnswerHistory = answerHistory;
        return thisAnswerHistory != null
                ? new ResponseEntity<>(thisAnswerHistory, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AnswerHistory answerHistory){
        AnswerHistory newAnswer = answerHistoryService.create(answerHistory);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") AnswerHistory answerHistoryFromDB,
                                    @RequestBody AnswerHistory answerHistory)
    {
        AnswerHistory updatedAnswerHistory = answerHistoryService.update(answerHistory, answerHistoryFromDB);
        if (updatedAnswerHistory != null) {
            return new ResponseEntity<>(updatedAnswerHistory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<AnswerHistory>> delete(@PathVariable("id") AnswerHistory answerHistory) {
        if (answerHistoryService.delete(answerHistory)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
