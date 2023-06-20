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

    //  method to fetch user by username
    @GetMapping("/users/{username}")
    private User getUserByName(@PathVariable("username") String username) {
        confirmUserName(username);

        Optional<User> optionalUser = this.userRepository.findUserByUserName(username);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username does not exist");
        }
        User existingUser = optionalUser.get();
        return existingUser;
    }

    // method to create new user
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewUser(@RequestBody User user) {
      confirmUser(user);
      this.userRepository.save(user);
      return user;  // OPTIONAL?
    }

    // method to update existing user, but username cannot be modified
    @PutMapping("/users/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private User updateUserData(@PathVariable("username") String userName, @RequestBody User updatedUser) {
        confirmUserName(userName);

        Optional<User> checkUserToUpdate = this.userRepository.findUserByUserName(userName);
        if (checkUserToUpdate.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Can't update non-existing uesr");
        }
        User existingUser = checkUserToUpdate.get();
        copyUserInfoFrom(updatedUser, existingUser);
        this.userRepository.save(existingUser);
        return existingUser;
    }

    private void copyUserInfoFrom(User updatedUser, User existingUser) {
        if (ObjectUtils.isEmpty(updatedUser.getUserName())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User is not allowed to be updated without providing a proper username.");
        }
        if (!ObjectUtils.isEmpty(updatedUser.getCity())) {
            existingUser.setCity(updatedUser.getCity());
        }
        if (!ObjectUtils.isEmpty(updatedUser.getState())) {
            existingUser.setState(updatedUser.getState());
        }
        if (!ObjectUtils.isEmpty(updatedUser.getZipCode())) {
            existingUser.setZipCode(updatedUser.getZipCode());
        }
        if (!ObjectUtils.isEmpty(updatedUser.getPeanutAllergy())) {
            existingUser.setPeanutAllergy(updatedUser.getPeanutAllergy());
        }
        if (!ObjectUtils.isEmpty(updatedUser.getDairyAllergy())) {
            existingUser.setDairyAllergy(updatedUser.getDairyAllergy());
        }
        if (!ObjectUtils.isEmpty(updatedUser.getEggAllergy())) {
            existingUser.setEggAllergy(updatedUser.getEggAllergy());
        }
    }

    // method to check if whether user already exists
    private void confirmUser(User user) {
        confirmUserName(user.getUserName());

        Optional<User> existingUser = this.userRepository.findUserByUserName(user.getUserName());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with the same username already exists");
        }
    }

    // helper method used to verify that the display name provided by the user is not null or empty
    private void confirmUserName(String userName) {
        if (ObjectUtils.isEmpty(userName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be null or empty");
        }
    }

}
