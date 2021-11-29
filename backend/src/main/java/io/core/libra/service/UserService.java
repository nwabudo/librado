package io.core.libra.service;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.UserDTO;
import io.core.libra.entity.User;

public interface UserService {

    ApiResponse<UserDTO> createUser(UserDTO userDTO);

    ApiResponse<UserDTO> userSearchByEmail(String email);

    UserDTO toModel(User user);

    User toEntity(UserDTO userDTO);
}
