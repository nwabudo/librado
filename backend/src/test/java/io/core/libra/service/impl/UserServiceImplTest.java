package io.core.libra.service.impl;

import io.core.libra.BaseTest;
import io.core.libra.dao.UserRepository;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.UserDTO;
import io.core.libra.entity.User;
import io.core.libra.exception.Messages;
import io.core.libra.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest extends BaseTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    private final String MESSAGE = "Expectations: %s || Actual: %s";
    private UserDTO userDTO = new UserDTO(1L, "emmox55@ymail.com", "Daniel", "Nwabudo");

    @Test
    @Order(1)
    @DisplayName("To test Successful user creation")
    void testCreateUser(){
        User user = userService.toEntity(userDTO);
        user.setId(userDTO.getId());
        doReturn(user).when(userRepository).save(any());

        UserDTO actual = userService.createUser(userDTO).getData();
        assertEquals(userDTO, actual, String.format(MESSAGE, userDTO, actual));
    }

    @Test
    @Order(2)
    @DisplayName("To test Fail user creation")
    void testCreateUser_Fail(){
        doReturn(true).when(userRepository).existsByEmail(any());

        ApiResponse<UserDTO> actual = userService.createUser(userDTO);
        assertEquals(null, actual.getData(), String.format(MESSAGE, null, actual.getData()));
        assertEquals(Messages.USER_RECORD_ALREADY_EXISTS.getMessage(), actual.getMessage(), "It should have same Message");
        assertEquals(false, actual.getStatus(), "Status for both should be same");
    }

    @Test
    @Order(3)
    @DisplayName("To test User can be Searched via Email")
    void testUserSearchByEmail(){
        User user = userService.toEntity(userDTO);
        user.setId(userDTO.getId());
        doReturn(Optional.of(user)).when(userRepository).findByEmail(any());

        ApiResponse<UserDTO> actual = userService.userSearchByEmail(userDTO.getEmail());
        assertEquals(userDTO, actual.getData(), String.format(MESSAGE, userDTO, actual.getData()));
    }

    @Test
    @DisplayName("To test that User Search will Fail for a Wrong Email Passed")
    void testUserSearchByEmail_Fail(){
        doReturn(Optional.empty()).when(userRepository).findByEmail(any());
        ApiResponse<UserDTO> actual = userService.userSearchByEmail("embox55Email.com");

        assertEquals(null, actual.getData(), String.format(MESSAGE, null, actual.getData()));
        assertEquals(Messages.NO_USER_RECORD_FOUND.getMessage(), actual.getMessage(), "It should have same Message");
        assertEquals(false, actual.getStatus(), "Status for both should be same");
    }
}