package com.albkin.diningreviewapi.controller;

import com.albkin.diningreviewapi.model.User;
import com.albkin.diningreviewapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewUser(@RequestBody User user) {
      confirmUser(user);
      this.userRepository.save(user);
      return user;  // OPTIONAL?
    }

    private void confirmUser(User user) {
        confirmUserName(user.getUserName());

        Optional<User> existingUser = this.userRepository.findUserByUserName(user.getUserName());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with the same username already exists");
        }
    }

    private void confirmUserName(String userName) {
        if (ObjectUtils.isEmpty(userName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be null or empty");
        }
    }

}
