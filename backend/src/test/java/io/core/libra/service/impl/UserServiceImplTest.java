package io.core.libra.service.impl;

import io.core.libra.BaseTest;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.UserModel;
import io.core.libra.exception.Messages;
import io.core.libra.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest extends BaseTest {

    @Autowired
    UserService userService;

    private final String MESSAGE = "Expectations: %s || Actual: %s";

    @Test
    @Order(1)
    @DisplayName("To test Successful user creation")
    void testCreateUser(){
        UserModel userModel = new UserModel
                ("emmox55@ymail.com", "Daniel", "Nwabudo");
        UserModel actual = userService.createUser(userModel).getData();
        assertEquals(userModel, actual, String.format(MESSAGE, userModel, actual));
    }

    @Test
    @Order(2)
    @DisplayName("To test Fail user creation")
    void testCreateUser_Fail(){
        UserModel userModel = new UserModel
                ("emmox55@ymail.com", "Daniel", "Nwabudo");
        // Create first Time
        userService.createUser(userModel);
        //Try the Creation again
        ApiResponse<UserModel> actual = userService.createUser(userModel);
        assertEquals(null, actual.getData(), String.format(MESSAGE, null, actual.getData()));
        assertEquals(Messages.USER_RECORD_ALREADY_EXISTS.getMessage(), actual.getMessage(), "It should have same Message");
        assertEquals(false, actual.getStatus(), "Status for both should be same");
    }

    @Test
    @Order(3)
    @DisplayName("To test Successful user creation")
    void testUserSearchByEmail(){

        UserModel userModel = new UserModel
                ("emmox55@ymail.com", "Daniel", "Nwabudo");
        userService.createUser(userModel);
        ApiResponse<UserModel> actual = userService.userSearchByEmail(userModel.getEmail());

        assertEquals(userModel, actual.getData(), String.format(MESSAGE, userModel, actual.getData()));
        assertEquals(userModel.getFirstName(), actual.getData().getFirstName(),
                String.format(MESSAGE, userModel.getFirstName(), actual.getData().getFirstName()));
    }

    @Test
    @DisplayName("To test Fail user creation")
    void testUserSearchByEmail_Fail(){
        ApiResponse<UserModel> actual = userService.userSearchByEmail("embox55Email.com");

        assertEquals(null, actual.getData(), String.format(MESSAGE, null, actual.getData()));
        assertEquals(Messages.NO_USER_RECORD_FOUND.getMessage(), actual.getMessage(), "It should have same Message");
        assertEquals(false, actual.getStatus(), "Status for both should be same");
    }
}