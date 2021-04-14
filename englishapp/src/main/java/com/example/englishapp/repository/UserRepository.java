package com.example.englishapp.repository;

import com.example.englishapp.entity.Statistic;
import com.example.englishapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);
    List<User> findByStatistics_Id(Integer id);
}
