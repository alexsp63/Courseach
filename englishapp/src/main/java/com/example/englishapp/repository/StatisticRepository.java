package com.example.englishapp.repository;

import com.example.englishapp.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Integer> {

}
