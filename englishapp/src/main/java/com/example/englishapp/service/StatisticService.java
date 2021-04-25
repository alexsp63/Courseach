package com.example.englishapp.service;

import com.example.englishapp.entity.Statistic;
import com.example.englishapp.repository.StatisticRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * сервис для работы с репозиторием статистики
 */
@Service
public class StatisticService {

    @Autowired //автоматически создает данный объект
    private StatisticRepository statisticRepository;

    /**
     * поиск записей в таблице статистики
     * @param userLogin - логин пользователя, для которого нужно найти все объекты статистики
     * @param lessonId - идентификатор урока, для которого нужно найти все объекты статистики
     * @return если передаваемые параметры null, то все записи из таблицы статистики,
     * если логин пользователя не равен null, а id урока равно, то список записей статистики, созданных передаваемым пользователем,
     * если логин пользователя null, а id урока не null, список статистик, созданных для передаваемого урока
     * если оба параметра не null, записи из таблицы статистики, созданные для передаваемого пользователя по передаваемому уроку
     */
    public List<Statistic> findAll(String userLogin, Integer lessonId){
        if (userLogin != null && lessonId != null){
            return statisticRepository.findAllByUser_LoginAndLesson_Id(userLogin, lessonId);
        } else if (userLogin != null && lessonId == null){
            return statisticRepository.findAllByUser_Login(userLogin);
        } else if (userLogin == null && lessonId != null){
            return statisticRepository.findAllByLesson_Id(lessonId);
        }
        return statisticRepository.findAll();
    }


    /**
     * создание записи в таблице статистики
     * @param statistic - создаваемая запись
     * @return созданная запись
     */
    public Statistic create(Statistic statistic){
        return statisticRepository.save(statistic);
    }

    /**
     * обновление уже существующей записи в таблице статистики
     * @param statistic - запись, на которую нужно обновить
     * @param statisticFromDB - запись, которую нужно обновить
     * @return обновлённую запись
     */
    public Statistic update(Statistic statistic, Statistic statisticFromDB) {
        BeanUtils.copyProperties(statistic, statisticFromDB, "id");
        return statisticRepository.save(statisticFromDB);
    }

    /**
     * удаление записи из таблицы статистики
     * @param statistic - запись, которую нужно удалить
     * @return true, если запись удалена, false - если нет
     */
    public boolean delete(Statistic statistic) {
        if (statistic != null){
            statisticRepository.delete(statistic);
            return true;
        }
        return false;
    }

}
