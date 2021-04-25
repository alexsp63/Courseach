package com.example.englishapp.repository;

import com.example.englishapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * интерфейс, служащий для выполнения базовых запросов к таблице пользователей
 */
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * поиск ползователя по логину
     * @param login - логин, по которому нужео найти пользователя
     * @return пользователя, которому принадлежит этот логин
     */
    Optional<User> findByLogin(String login);

    /**
     * поиск пользователя по идентификатору статистики
     * @param id - идентификатор статистики, по которому надо найти пользователя
     * @return пользователя, для которого был создан переданный объект статистики
     */
    List<User> findByStatistics_Id(Integer id);
}
