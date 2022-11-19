package com.kris.api_server.controllers;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kris.api_server.models.User;
import com.kris.api_server.requestMappers.CreateUserRequest;
import com.kris.api_server.requestMappers.UpdateUserRequest;
import com.kris.api_server.service.UserService;

@RestController
public class UserController {
    
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(produces = "application/json", path = "/users")
    public List<User> getAll(){
        return userService.getAll();
    }

    @GetMapping(produces = "application/json", path = "/users/{id}")
    @ResponseBody
    public User getUserByID(@PathVariable("id") int id){
       Optional<User> optUser = userService.getUserByID(id);
        if(optUser.isPresent()){
            return optUser.get();
        }
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
    }

    @PostMapping(produces = "application/json", value = "/users")
    public User creatUser(@Valid @RequestBody CreateUserRequest user){
         return userService.create(user);
    }

    @PutMapping(produces = "application/json", value = "/users/{id}")
    @ResponseBody
    public User updateUser(@PathVariable("id") int id, @RequestBody @Valid UpdateUserRequest updateUserRequest){
        try {
            return userService.update(id, updateUserRequest.getName(), updateUserRequest.getEmail());
        } catch(IllegalArgumentException e) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "user not found");
        }
        
    }

    @DeleteMapping(produces = "application/json", value = "/users/{id}")
    @ResponseBody
    public User deleteUser(@PathVariable("id") int id){
       try {
         return userService.delete(id);
       } catch(IllegalArgumentException e){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "user not found");
       } 
       }

}
