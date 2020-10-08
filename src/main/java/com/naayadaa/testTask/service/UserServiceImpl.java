package com.naayadaa.testTask.service;

import com.naayadaa.testTask.domain.User;
import com.naayadaa.testTask.dto.CreateUserResponse;
import com.naayadaa.testTask.dto.UserMapper;
import com.naayadaa.testTask.dto.UserRequest;
import com.naayadaa.testTask.dto.UserResponse;
import com.naayadaa.testTask.exception.BadRequestError;
import com.naayadaa.testTask.exception.NotFoundError;
import com.naayadaa.testTask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CreateUserResponse create(UserRequest userDto) {
        Optional<User> user = repository.findByLogin(userDto.getLogin());
        checkLogin(userDto.getLogin());
        User newUser = mapper.map(userDto);
        newUser.setUuid(UUID.randomUUID().toString());
        newUser = repository.save(newUser);
        return new CreateUserResponse(newUser.getUuid());
    }

    @Override
    public UserResponse update(String uuid, UserRequest userDto) {
        User user = repository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundError("User not found"));
        if (!userDto.getLogin().equals(user.getLogin())) {
            checkLogin(userDto.getLogin());
        }
        user.setLogin(userDto.getLogin());
        user.setBirthDate(userDto.getBirthDate());
        user.setFullName(userDto.getFullName());

        repository.save(user);
        return mapper.map(user);
    }

    private void checkLogin(String login) {
        Optional<User> user = repository.findByLogin(login);
        if(user.isPresent()) {
            throw new BadRequestError("Login already exist");
        }
    }

    @Override
    public UserResponse get(String uuid) {
        User user = repository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundError("User not found"));

        return mapper.map(user);
    }
}
