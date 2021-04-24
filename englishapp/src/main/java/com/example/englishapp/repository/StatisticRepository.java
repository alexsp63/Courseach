package com.example.englishapp.repository;

import com.example.englishapp.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * интерфейс, реализующий базовые методы для работы с таблицей статистики
 */
public interface StatisticRepository extends JpaRepository<Statistic, Integer> {
    /**
     * поиск всех объектов статистики по внешним ключам - по логину пользователя и по id урока
     * @param userLogin - логин пользователя, для которого необходимо найти статистику
     * @param lessonId - id урока, для которого необходимо найти статистику
     * @return список объектов статистики, которые найдены по передаваемому идентификатору урока для передаваемого пользователя
     */
    List<Statistic> findAllByUser_LoginAndLesson_Id(String userLogin, Integer lessonId);

    /**
     * поиск статистики только по логину пользователя
     * @param userLogin - логин пользователя, для которого надо найти все существующие объекты статистики
     * @return - все объекты статистики, созданные для данного пользователя
     */
    List<Statistic> findAllByUser_Login(String userLogin);

    /**
     * поиск статистики, сформированной по конкретному уроку
     * @param lessonId - идентификатор урока, для которого нужно найти все объекты статистики
     * @return - все объекты статистики, найденные для данного урока
     */
    List<Statistic> findAllByLesson_Id(Integer lessonId);
}
