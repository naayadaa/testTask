package com.naayadaa.testTask.service;

import com.naayadaa.testTask.dto.CreateUserResponse;
import com.naayadaa.testTask.dto.UserRequest;
import com.naayadaa.testTask.dto.UserResponse;

public interface UserService {

    CreateUserResponse create(UserRequest user);

    UserResponse update(String uuid, UserRequest userDto);

    UserResponse get(String uuid);

}
