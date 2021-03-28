package com.example.englishapp.service;

import com.example.englishapp.entity.Statistic;
import com.example.englishapp.repository.StatisticRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticService {

    @Autowired //автоматич создает данный объект
    private StatisticRepository statisticRepository;


    public List<Statistic> findAll(){
        return statisticRepository.findAll();
    }


    public Statistic create(Statistic statistic){
        return statisticRepository.save(statistic);
    }

    public Statistic update(Statistic statistic, Statistic statisticFromDB) {
        BeanUtils.copyProperties(statistic, statisticFromDB, "id");
        return statisticRepository.save(statisticFromDB);
    }

    public boolean delete(Statistic statistic) {
        if (statistic != null){
            statisticRepository.delete(statistic);
            return true;
        }
        return false;
    }

}
