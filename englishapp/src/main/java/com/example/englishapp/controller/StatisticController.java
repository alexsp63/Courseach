package com.example.englishapp.controller;


import com.example.englishapp.entity.Statistic;
import com.example.englishapp.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    public ResponseEntity<List<Statistic>> readAll(
            @RequestParam(required=false) String userLogin,
            @RequestParam(required=false) Integer lessonId
    ){
        final List<Statistic> statisticList = statisticService.findAll(userLogin, lessonId);
        return statisticList != null && !statisticList.isEmpty()
                ? new ResponseEntity<>(statisticList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Statistic> getOne(@PathVariable(name = "id") Statistic statistic){
        final Statistic thisStatistic = statistic;
        return thisStatistic != null
                ? new ResponseEntity<>(thisStatistic, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Statistic statistic){
        Statistic newAnswer = statisticService.create(statistic);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Statistic statisticFromDB,
                                    @RequestBody Statistic statistic)
    {
        Statistic updatedStatistic = statisticService.update(statistic, statisticFromDB);
        if (updatedStatistic != null) {
            return new ResponseEntity<>(updatedStatistic, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Statistic>> delete(@PathVariable("id") Statistic statistic) {
        if (statisticService.delete(statistic)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
