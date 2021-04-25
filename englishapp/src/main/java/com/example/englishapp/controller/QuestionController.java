package com.example.englishapp.controller;

import com.example.englishapp.entity.Question;
import com.example.englishapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * контроллер, отвечающий за взаимодействие запросов с таблицей вопросов
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * конструктор
     * @param questionService - созданный сервис вопросов
     */
    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * получение записей из таблицы вопросов от пользователей, имеющих право на чтение таблицы
     * @param lessonId - необязательный параметр - идентификатор урока, по которому нужно найти все вопросы, принадлежащие ему
     * @return список возвращаемых вопросов и хороший статус или статус "не найдено"
     */
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

    /**
     * получение записи из таблицы вопросов по уникальному идентификатору для пользователей, имеющих право на чтение таблицы
     * @param question - найденный по передаваемому идентификатору вопрос
     * @return найденный вопрос и хороший статус или статус "не найдено"
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('table:read')")
    public ResponseEntity<Question> getOne(@PathVariable(name = "id") Question question){
        final Question thisQuestion = question;
        return thisQuestion != null
                ? new ResponseEntity<>(thisQuestion, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * добавление записи в таблицу вопросов пользователями, имеющими право на запись
     * @param question - вопрос, который нужно записать
     * @return - созданную запись и хороший статус
     */
    @PostMapping
    @PreAuthorize("hasAuthority('table:write')")
    public ResponseEntity<?> create(@RequestBody Question question){
        Question newAnswer = questionService.create(question);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    /**
     * обновление уже существующей записи в таблице вопросов пользователями, имеющими право на редактирование таблицы
     * @param questionFromDB - найденная по передаваемому идентификатору уже существующая запись
     * @param question - передаваемая запись, на что нужно обновить
     * @return либо обновлённую запись, либо 404 статус
     */
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

    /**
     * удаление вопроса из таблицы пользователем, имеющим на это право
     * @param question - вопрос, который нужно удалить
     * @return или хороший статус при успешном удалении, или статус "не найдено"
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('table:write')")
    public ResponseEntity<List<Question>> delete(@PathVariable("id") Question question) {
        if (questionService.delete(question)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
