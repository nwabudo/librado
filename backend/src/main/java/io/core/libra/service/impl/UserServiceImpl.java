package io.core.libra.service.impl;

import io.core.libra.dao.UserRepository;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.UserModel;
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
    public ApiResponse<UserModel> createUser(UserModel userModel) {
        if(userRepository.existsByEmail(userModel.getEmail()))
            return new ApiResponse<>(Messages.USER_RECORD_ALREADY_EXISTS.getMessage(), false);

        User user = toEntity(userModel);
        user = userRepository.save(user);
        return new ApiResponse<>(toModel(user));
    }

    @Override
    public ApiResponse<UserModel> userSearchByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) return new ApiResponse<>(Messages.NO_USER_RECORD_FOUND.getMessage(), false);
        return new ApiResponse<>(toModel(user));
    }

    private UserModel toModel(User user) {
        if(user == null) return null;
        return UserModel.builder()
                .email(user.getEmail())
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    private User toEntity(UserModel userModel) {
        if(userModel == null) return null;
        return new User(userModel.getFirstName(), userModel.getLastName(), userModel.getEmail());
    }


}
