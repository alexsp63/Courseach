package com.example.englishapp.controller;

import com.example.englishapp.entity.User;
import com.example.englishapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * контроллер, отвечающий за запросы к таблице пользователей
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * конструтор
     * @param userService - созданный сервис для работы с таблицей пользователей
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * получение записей из таблицы пользователей
     * @param statisticsId - необязательный аргумент - идентификатор статистики, по которому нужно искать пользователя
     * @return или найденных пользователей, или статус "не найдено"
     */
    @GetMapping
    public ResponseEntity<List<User>> getAll(
            @RequestParam(required=false) Integer statisticsId
    ){
        final List<User> userList = userService.findAll(statisticsId);
        return userList != null && !userList.isEmpty()
                ? new ResponseEntity<>(userList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * получение записи из таблицы пользователя по его уникальному идентификатору - логину
     * @param user - найденный по передаваемому логину пользователь
     * @return или найденного пользователя, или статус "не найдено"
     */
    @GetMapping("/{login}")
    public ResponseEntity<User> getOne(@PathVariable(name = "login") User user){
        final User thisUser = user;
        return thisUser != null
                ? new ResponseEntity<>(thisUser, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * получение всех существующих логинов пользователей
     * @return или все занятые логины, или статус, что ничего не найдено
     */
    @GetMapping("/logins")
    public ResponseEntity<List<String>> getLogins(){
        final List<String> loginList = userService.findAllLogins();
        return loginList != null && !loginList.isEmpty()
                ? new ResponseEntity<>(loginList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * добавление записи в таблицу пользователей
     * @param user - добавляемый пользователь
     * @return добавленный пользователь
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user){
        User newUser = userService.create(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * обновление уже существующей записи в таблице пользователей
     * @param userFromDB - найденный по логину уже существующий пользователь, информацию о котором надо обновить
     * @param user - то, на что нужно обновить запись
     * @return или обновленного пользователя, или статус "не найдено"
     */
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

    /**
     * удаление пользователя из таблицы
     * @param user - удаляемый пользователь
     * @return или успешный статус, или статус, что запись не найдена
     */
    @DeleteMapping("/{login}")
    public ResponseEntity<List<User>> delete(@PathVariable("login") User user) {
        if (userService.delete(user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
