package io.core.libra.service;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.UserModel;

public interface UserService {

    ApiResponse<UserModel> createUser(UserModel userModel);

    ApiResponse<UserModel> userSearchByEmail(String email);
}
