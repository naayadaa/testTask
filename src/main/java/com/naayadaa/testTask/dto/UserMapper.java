package com.naayadaa.testTask.dto;

import com.naayadaa.testTask.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserRequest userDto);

    @Mapping(target = "id", source = "uuid")
    UserResponse map(User user);
}
