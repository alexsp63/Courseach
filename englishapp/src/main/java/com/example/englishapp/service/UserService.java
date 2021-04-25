package com.example.englishapp.service;

import com.example.englishapp.entity.User;
import com.example.englishapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * сервис для работы с пользовательским репозиторием
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * поиск записей в таблице пользователей
     * @param statisticsId - id статистики, для которого нужно найти пользователя
     * @return либо всех пользователей, если параметр пустой, либо пользователя, имеющего передаваемое значение статистики
     */
    public List<User> findAll(Integer statisticsId){
        if (statisticsId != null) {
            return userRepository.findByStatistics_Id(statisticsId);
        }
        return userRepository.findAll();
    }

    /**
     * поиск всех занятых логинов
     * @return список всех логинов, которые уже заняты
     */
    public List<String> findAllLogins(){
        List<String> allLogins = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList){
            allLogins.add(user.getLogin());
        }
        return allLogins;
    }

    /**
     * создание записи в таблице пользователей
     * @param user - запись, которую нужно создать
     * @return созданную запись
     */
    public User create(User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    /**
     * обновление текущей записи в таблице пользователей с избежанием повторного хеширование уже захешированного пароля
     * @param user - запись, на которую нужно обновить уже существующую запись
     * @param userFromDB - уже существующая запись, которую нужно обновить
     * @return обновленную запись
     */
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

    /**
     * удаление записи из таблицы пользователей
     * @param user запись которую нужно удалить
     * @return результат удаление, true - если удалилось, false - если нет
     */
    public boolean delete(User user) {
        if (user != null){
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}
