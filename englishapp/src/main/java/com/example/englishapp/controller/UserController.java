package com.example.englishapp.controller;

import com.example.englishapp.entity.User;
import com.example.englishapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> readAll(){
        final List<User> userList = userService.findAll();
        return userList != null && !userList.isEmpty()
                ? new ResponseEntity<>(userList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //получение значения по login
    @GetMapping("/{login}")
    public ResponseEntity<User> getOne(@PathVariable(name = "login") User user){
        final User thisUser = user;
        return thisUser != null
                ? new ResponseEntity<>(thisUser, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/logins")
    public ResponseEntity<List<String>> getLogins(){
        final List<String> loginList = userService.findAllLogins();
        return loginList != null && !loginList.isEmpty()
                ? new ResponseEntity<>(loginList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //добавление записи
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user){
        User newUser = userService.create(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    //обновление конкретной записи, которая уже существует
    @PutMapping("/{login}")
    public ResponseEntity<?> update(@PathVariable(name = "login") User userFromDB, //user из бд
                                    @RequestBody User user) //user от пользователя
    {
        User updatedUser = userService.update(user, userFromDB);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{login}")
    public ResponseEntity<List<User>> delete(@PathVariable("login") User user) {
        if (userService.delete(user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
