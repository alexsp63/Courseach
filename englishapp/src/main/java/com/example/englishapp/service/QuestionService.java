package com.example.englishapp.service;

import com.example.englishapp.entity.Question;
import com.example.englishapp.repository.QuestionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * сервис, работающий с репозиторием вопросов
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;


    /**
     * метод для поиска записей в таблице вопросов
     * @param lessonId - id урока, для которого надо найти все существующие вопросы
     * @return если ищем для конкретного урока, то список вопросов, добавленных в этот урок, иначе - список всех существующих вопросов
     */
    public List<Question> findAll(Integer lessonId){
        if (lessonId != null){
            return questionRepository.findAllByLessonId_Id(lessonId);
        }
        return questionRepository.findAll();
    }


    /**
     * создание новой записи в таблице вопросов
     * @param question - запись, которую нужно создать
     * @return созданную запись
     */
    public Question create(Question question){
        return questionRepository.save(question);
    }

    /**
     * обновление записи в таблице вопросов
     * @param question - запись, на которую надо обновить уже существующую
     * @param questionFromDB - уже существующая запись, которую нужно обновить
     * @return обновлённую запись
     */
    public Question update(Question question, Question questionFromDB) {
        BeanUtils.copyProperties(question, questionFromDB, "id");
        return questionRepository.save(questionFromDB);
    }

    /**
     * удаление записи из таблицы вопросов
     * @param question вопрос, который нужно удалить
     * @return true, если удаление прошло успешно, и false, если нет
     */
    public boolean delete(Question question) {
        if (question != null){
            questionRepository.delete(question);
            return true;
        }
        return false;
    }
}
