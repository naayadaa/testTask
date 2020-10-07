package com.naayadaa.testTask.controller;

import com.naayadaa.testTask.dto.CreateUserResponse;
import com.naayadaa.testTask.dto.UserRequest;
import com.naayadaa.testTask.dto.UserResponse;
import com.naayadaa.testTask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public CreateUserResponse create(@Valid @RequestBody UserRequest user) {
        return userService.create(user);
    }

    @PutMapping("/user/{id}")
    public UserResponse update(@Valid @RequestBody UserRequest user, @PathVariable String id) {
        return userService.update(id, user);
    }

    @GetMapping("/user/{id}")
    public UserResponse getById(@PathVariable String id) {
        return userService.get(id);
    }
}
