package com.awesome.userservice.util;


import com.awesome.userservice.dto.UserDto;
import com.awesome.userservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserDto dto);

    UserDto toUserDto(User unit);


}
