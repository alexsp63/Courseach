package com.example.englishapp.controller;

import com.example.englishapp.entity.Test;
import com.example.englishapp.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public ResponseEntity<List<Test>> readAll(){
        final List<Test> testList = testService.findAll();
        return testList != null && !testList.isEmpty()
                ? new ResponseEntity<>(testList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getOne(@PathVariable(name = "id") Test test){
        final Test thisTest = test;
        return thisTest != null
                ? new ResponseEntity<>(thisTest, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Test test){
        Test newAnswer = testService.create(test);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Test testFromDB,
                                    @RequestBody Test test)
    {
        Test updatedTest = testService.update(test, testFromDB);
        if (updatedTest != null) {
            return new ResponseEntity<>(updatedTest, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Test>> delete(@PathVariable("id") Test test) {
        if (testService.delete(test)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
