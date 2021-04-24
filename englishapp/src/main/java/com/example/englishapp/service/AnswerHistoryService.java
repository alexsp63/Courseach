package com.example.englishapp.service;

import com.example.englishapp.entity.AnswerHistory;
import com.example.englishapp.repository.AnswerHistoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * сервис для работы с пользовательским репозиторием
 */
@Service
public class AnswerHistoryService {

    @Autowired
    private AnswerHistoryRepository answerHistoryRepository;

    /**
     * либо ищет все объекты истории ответов, либо только по передаваемому id статистики
     * @param statisticsId - id статистики, для которой нужно найти все истории ответов (может быть null)
     * @return все записи из таблицы истории ответов, если передаваемый параметр null, иначе возвращает все записи, у которых id статистики равно принимаемому параметру
     */
    public List<AnswerHistory> findAll(Integer statisticsId){
        if (statisticsId != null){
            return answerHistoryRepository.findAllByStatistic_Id(statisticsId);
        }
        return answerHistoryRepository.findAll();
    }

    /**
     * создание записи в таблице истории ответов
     * @param answerHistory - запись, которую нужно создать
     * @return - созданную запись
     */
    public AnswerHistory create(AnswerHistory answerHistory){
        return answerHistoryRepository.save(answerHistory);
    }

    /**
     * обновление записи в таблице истории ответов
     * @param answerHistory - запись, на которую надо заменить уже существующую запись
     * @param answerHistoryFromDB - уже существующая запись, которую надо заменить
     * @return измененную запись
     */
    public AnswerHistory update(AnswerHistory answerHistory, AnswerHistory answerHistoryFromDB) {
        BeanUtils.copyProperties(answerHistory, answerHistoryFromDB, "id");
        return answerHistoryRepository.save(answerHistoryFromDB);
    }

    /**
     * удаление записи в таблицы истории ответов
     * @param answerHistory - запись, которую надо удалить
     * @return - true, если запись удалена, false - если удаление не произошло
     */
    public boolean delete(AnswerHistory answerHistory) {
        if (answerHistory != null){
            answerHistoryRepository.delete(answerHistory);
            return true;
        }
        return false;
    }
}
