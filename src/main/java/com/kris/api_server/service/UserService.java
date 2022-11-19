package com.kris.api_server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kris.api_server.models.User;
import com.kris.api_server.repositories.UserRepository;
import com.kris.api_server.requestMappers.CreateUserRequest;


@Service
public class UserService {
    
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    public List<User> getAll(){
        return userRepository.findAll();
    }
    
    public Optional<User> getUserByID(int id){
        return userRepository.findById(id);
    }

    @Transactional
    public User create(CreateUserRequest createUserRequest){
        User user = new User(createUserRequest.getName(), createUserRequest.getEmail()); 
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public User update(int id, String name, String email) throws IllegalArgumentException{
      Optional<User> optUser = userRepository.findById(id);
      if(optUser.isPresent()){
        User user = optUser.get();
        user.setName(name);
        user.setEmail(email);
        return userRepository.saveAndFlush(user);
      }
      throw new IllegalArgumentException("User does not exist");
    }

    @Transactional
    public User delete(int id)throws IllegalArgumentException{
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isPresent()){
        User tmpUser = optUser.get();
        userRepository.deleteById(id);
        userRepository.flush();
        return tmpUser;
        }
        throw new IllegalArgumentException();
    }
}
