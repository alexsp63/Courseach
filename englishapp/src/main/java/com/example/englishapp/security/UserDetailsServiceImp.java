package com.example.englishapp.security;

import com.example.englishapp.entity.User;
import com.example.englishapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Получение данных пользователя
 */
@Service("userDetailsServiceImp")
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Конструктор
     * @param userRepository - созданный пользовательский репозиторий
     */
    @Autowired
    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Поиск пользователя в базе данных по логину
     * @param login - логин, по которому надо попробовать авторизовать пользователя
     * @return секьюрного пользователя
     * @throws UsernameNotFoundException исключение
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exist"));
        return SecurityUser.fromUser(user);
    }
}
