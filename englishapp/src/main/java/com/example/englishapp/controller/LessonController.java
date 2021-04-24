package com.example.englishapp.controller;

import com.example.englishapp.entity.Lesson;
import com.example.englishapp.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * контроллер, отвечающий за взаимодействие с таблицей уроков запросов по ссылке уроков
 */
@RestController
@RequestMapping("/lesson")
public class LessonController {

    private final LessonService lessonService;

    /**
     * конструктор
     * @param lessonService - созданный сервис уроков, к которому будем обращаться для обращения к базе данных
     */
    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    /**
     * получение записей из таблицы уроков для пользователей, имующих право на стение записей этой таблицы
     * @param questionId - необязательный параметр - id вопроса, по которому нужно найти урок, имеющий этот вопрос
     * @return либо список найденных записей, либо статус "не найден"
     */
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

    /**
     * получение урока по его уникальному идентификатору для пользователей, имеющих право на чтение этой таблицы
     * @param lesson - найденный по передаваемому идентификатору урок
     * @return либо найденный урок и хороший статус, либо статус "не найдено"
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('table:read')")
    public ResponseEntity<Lesson> getOne(@PathVariable(name = "id") Lesson lesson){
        final Lesson thisLesson = lesson;
        return thisLesson != null
                ? new ResponseEntity<>(thisLesson, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * добавление записи в таблицу уроков пользователями, имеющими право на изменение таблицы
     * @param lesson - урок, который необходимо добавить
     * @return - добавленный урок и хороший статус
     */
    @PostMapping
    @PreAuthorize("hasAuthority('table:write')")
    public ResponseEntity<?> create(@RequestBody Lesson lesson){
        Lesson newAnswer = lessonService.create(lesson);
        return new ResponseEntity<>(newAnswer, HttpStatus.CREATED);
    }

    /**
     * изменение уже существующей записи из таблицы уроков пользователями, имеющими право на изменение таблицы
     * @param lessonFromDB - найденная по передаваемому идентификатору существующая запись из таблицы уроков
     * @param lesson - переданная запись, на которую нужно изменить уже существующую
     * @return при успешном обновлении - обновленную запись и хороший статус, иначе - статус, что запись не найдена
     */
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

    /**
     * удаление записи из таблицы уроков пользователями, имеющими право на изменение этой таблицы
     * @param lesson - найденный по передаваемому идентификатору урок для удаления
     * @return хороший статус при успешном удалении и "не найдено" - если удаления не произошло
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('table:write')")
    public ResponseEntity<List<Lesson>> delete(@PathVariable("id") Lesson lesson) {
        if (lessonService.delete(lesson)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
