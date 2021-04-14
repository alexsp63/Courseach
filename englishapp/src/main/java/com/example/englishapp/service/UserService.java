package com.example.englishapp.service;

import com.example.englishapp.config.SecurityConfig;
import com.example.englishapp.entity.User;
import com.example.englishapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public List<User> findAll(Integer statisticsId){
        if (statisticsId != null) {
            return userRepository.findByStatistics_Id(statisticsId);
        }
        return userRepository.findAll();
    }

    public List<String> findAllLogins(){
        List<String> allLogins = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList){
            allLogins.add(user.getLogin());
        }
        return allLogins;
    }

    public User create(User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User update(User user, User userFromDB) {
        if (!user.getPassword().equals(userFromDB.getPassword())){
            //если пользователь решил изменить пароль, иначе не надо хешировать уже хешированный пароль
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
        }
        BeanUtils.copyProperties(user, userFromDB, "login");
        return userRepository.save(userFromDB);
    }

    public boolean delete(User user) {
        if (user != null){
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}
