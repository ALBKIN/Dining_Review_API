package com.albkin.diningreviewapi.controller;

import com.albkin.diningreviewapi.model.User;
import com.albkin.diningreviewapi.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // method to fetch all users
    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return this.userRepository.findAll();
    }
    // method to fetch user by ID
    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable("id") Long id) {
        return this.userRepository.findById(id);
    }
    // method to create new user
    @PostMapping("/users")
    public User createNewUser(@RequestBody User user) {
        User newUser = this.userRepository.save(user);
        return user;
    }


}
