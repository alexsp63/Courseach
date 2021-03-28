package com.example.englishapp.service;

import com.example.englishapp.entity.User;
import com.example.englishapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired //автоматич создает данный объект
    private UserRepository userRepository;


    public List<User> findAll(){
        return userRepository.findAll();
    }


    public User create(User user){
        return userRepository.save(user);
    }

    public User update(User user, User userFromDB) {
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
