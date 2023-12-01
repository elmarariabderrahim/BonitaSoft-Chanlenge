package com.bonitasoft.cookingapp.service;

import com.bonitasoft.cookingapp.entity.User;
import com.bonitasoft.cookingapp.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    public User findUser(String userName) {
        return userRepository.getByUsername(userName);
    }


}
