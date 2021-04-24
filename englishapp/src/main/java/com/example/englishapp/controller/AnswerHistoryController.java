package com.example.englishapp.controller;

import com.example.englishapp.entity.AnswerHistory;
import com.example.englishapp.service.AnswerHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * контроллер, связывающий запросы по ссылкам с /answer_history с взаимодействием с таблицей истории ответов
 */
@RestController
@RequestMapping("/answer_history")
public class AnswerHistoryController {

    private final AnswerHistoryService answerHistoryService;

    /**
     * конструктор
     * @param answerHistoryService - сервис для работы с таблицей истории ответов
     */
    @Autowired
    public AnswerHistoryController(AnswerHistoryService answerHistoryService) {
        this.answerHistoryService = answerHistoryService;
    }

    /**
     * осуществление метода GET
     * @param statisticsId - необязательный параметр id статистики (для поиска записей истории ответов с таким id статистики)
     * @return сущность ответа, либо список записей и 20* статус, либо 404 статус
     */
    @GetMapping
    public ResponseEntity<List<AnswerHistory>> readAll(
            @RequestParam(required=false) Integer statisticsId
    ){
        final List<AnswerHistory> answerHistoryList = answerHistoryService.findAll(statisticsId);
        return answerHistoryList != null && !answerHistoryList.isEmpty()
                ? new ResponseEntity<>(answerHistoryList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * получение записи из таблицы истории ответов по первичному ключу
     * @param answerHistory - запись, которой принадлежит первичный ключ, переданный пользователем
     * @return либо найденную запись и OK-статус, либо 404 статус (запись не найдена)
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnswerHistory> getOne(@PathVariable(name = "id") AnswerHistory answerHistory){
        final AnswerHistory thisAnswerHistory = answerHistory;
        return thisAnswerHistory != null
                ? new ResponseEntity<>(thisAnswerHistory, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * создание записи в таблице истории ответов
     * @param answerHistory - запись, которую нужно создать
     * @return созданную запись и хороший статус ответа
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody AnswerHistory answerHistory){
        AnswerHistory newAnswer = answerHistoryService.create(answerHistory);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    /**
     * обновление записи в таблице истории ответов
     * @param answerHistoryFromDB - найденная по передаваемому идентификатору существующая запись истории ответов
     * @param answerHistory - запись, на которую нужно обновить уже существующую
     * @return либо обновлённую запись и хороший http статус, либо статус 404 (если не найдена запись, которую надо обновить)
     */
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

    /**
     * удалении записи из таблицы истории ответов
     * @param answerHistory - найденная по передаваемому id запись, которую нужно удалить
     * @return хороший статус, если запись удалена, или 404 статус, если запись для удаления не найдена
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<List<AnswerHistory>> delete(@PathVariable("id") AnswerHistory answerHistory) {
        if (answerHistoryService.delete(answerHistory)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
