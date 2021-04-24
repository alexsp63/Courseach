package com.example.englishapp.service;

import com.example.englishapp.entity.Lesson;
import com.example.englishapp.repository.LessonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * сервис, работающий с репозиторием уроков
 */
@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    /**
     * поиск либо всех уроков, либо урока, которой содержить передаваемый вопрос
     * @param questionId - id вопроса, для которого надо найти урок, в который он был добавлен
     * @return - если передаваемый параметр null, список всех уроков, иначе - урок, в котором добавлен передаваемый вопрос
     */
    public List<Lesson> findAll(Integer questionId){
        if (questionId != null){
            return lessonRepository.findByQuestions_Id(questionId);
        }
        return lessonRepository.findAll();
    }

    /**
     * создание записи в таблице уроков
     * @param lesson - урок, который нужно создать
     * @return созданный урок
     */
    public Lesson create(Lesson lesson){
        return lessonRepository.save(lesson);
    }

    /**
     * обновление уже существующей записи в таблице уроков
     * @param lesson - запись, на которую нужно обновить уже существующую
     * @param lessonFromDB - существующая запись, которую нужно обновить
     * @return обновленную запись
     */
    public Lesson update(Lesson lesson, Lesson lessonFromDB) {
        BeanUtils.copyProperties(lesson, lessonFromDB, "id");
        return lessonRepository.save(lessonFromDB);
    }

    /**
     * удаление существующей записи из таблицы уроков
     * @param lesson - запись, которую нужно удалить
     * @return true, если удаление прошло успешно, и false - если нет
     */
    public boolean delete(Lesson lesson) {
        if (lesson != null){
            lessonRepository.delete(lesson);
            return true;
        }
        return false;
    }
}
