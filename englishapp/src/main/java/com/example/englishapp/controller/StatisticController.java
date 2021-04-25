package com.example.englishapp.controller;


import com.example.englishapp.entity.Statistic;
import com.example.englishapp.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * контроллер, отвечающий за запросы к базе данных статистики
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController {

    private final StatisticService statisticService;

    /**
     * конструктор
     * @param statisticService - созданный сервис статистики
     */
    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    /**
     * получение записей из таблицы статистики
     * @param userLogin - необязательный параметр, логин пользователя, для которого надо искать статистику
     * @param lessonId - необязательный параметр, идентификатор урока, для которого надо искать статистику
     * @return - ответ, найденные записи и OK-статус или 404 статус, если ничего не найдено
     */
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

    /**
     * получение одной записи по её идентификатору
     * @param statistic - найденная запись по переданному пользователем идентификатору
     * @return или найденную запись, или статус "не найдено"
     */
    @GetMapping("/{id}")
    public ResponseEntity<Statistic> getOne(@PathVariable(name = "id") Statistic statistic){
        final Statistic thisStatistic = statistic;
        return thisStatistic != null
                ? new ResponseEntity<>(thisStatistic, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * создание записи в таблице статистики
     * @param statistic - запись, которую нужно создать
     * @return созданную запись
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Statistic statistic){
        Statistic newAnswer = statisticService.create(statistic);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    /**
     * обновление уже существующей записи в таблице статистики
     * @param statisticFromDB - найденная по идентификатору запись, которую нужно обновить
     * @param statistic - на что нужно обновить
     * @return или обновленную запись, или статус "не найдено"
     */
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

    /**
     * удаление записи из таблицы статистики
     * @param statistic - запись, которую нужно удалить
     * @return или хороший статус, если удаление пролшло успешно, или статус, что запись не найдена
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<List<Statistic>> delete(@PathVariable("id") Statistic statistic) {
        if (statisticService.delete(statistic)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
