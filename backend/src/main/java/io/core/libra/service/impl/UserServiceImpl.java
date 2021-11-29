package io.core.libra.service.impl;

import io.core.libra.dao.UserRepository;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.UserDTO;
import io.core.libra.entity.User;
import io.core.libra.exception.Messages;
import io.core.libra.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ApiResponse<UserDTO> createUser(UserDTO userDTO) {
        if(userRepository.existsByEmail(userDTO.getEmail()))
            return new ApiResponse<>(Messages.USER_RECORD_ALREADY_EXISTS.getMessage(), false);

        User user = toEntity(userDTO);
        user = userRepository.save(user);
        return new ApiResponse<>(toModel(user));
    }

    @Override
    public ApiResponse<UserDTO> userSearchByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) return new ApiResponse<>(Messages.NO_USER_RECORD_FOUND.getMessage(), false);
        return new ApiResponse<>(toModel(user));
    }

    @Override
    public UserDTO toModel(User user) {
        if(user == null) return null;
        return UserDTO.builder()
                .email(user.getEmail())
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if(userDTO == null) return null;
        return new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
    }


}
