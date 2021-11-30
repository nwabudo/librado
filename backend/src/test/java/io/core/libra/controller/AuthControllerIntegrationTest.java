package io.core.libra.controller;

import io.core.libra.BaseTest;
import io.core.libra.dtos.LoginDTO;
import io.core.libra.dtos.UserDTO;
import io.core.libra.exception.Messages;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static io.core.libra.JsonString.asJsonString;
import static io.core.libra.exception.Messages.METHOD_NOT_SUPPORTED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIntegrationTest extends BaseTest {

    private final String URL_BASE = "/api/v1/auth";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("To Test a successful User Signup Process")
    void testUserSignUp() throws Exception {
        UserDTO userDTO = new UserDTO
                ("emmox55@gmail.com", "Emmanuel", "Nwabudo");
        userSignUp(userDTO, "$.message", Messages.SUCCESS.getMessage(), status().isCreated());
    }

    @Test
    @Order(2)
    @DisplayName("To Test a failed Signup process using invalid userModel inputs")
    void testUserSignUp_Fail_1() throws Exception {
        UserDTO userDTO = new UserDTO
                ("embox55gmail.com", "Emmanuel", "Nwabudo");
        userSignUp(userDTO, "$.message", Messages.VALIDATION_ERRORS.getMessage(), status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("To Test a failed Signup process using a wrong request Method")
    void testUserSignUp_Fail_2() throws Exception {
        UserDTO userDTO = new UserDTO
                ("embox55gmail.com", "Emmanuel", "Nwabudo");

        mockMvc.perform(put(URL_BASE + "/signup")
                        .contentType(APPLICATION_JSON).content(asJsonString(userDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Is.is(METHOD_NOT_SUPPORTED.getMessage() + " POST")))
                .andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("To Test a successful User Login Process")
    void testFindUserByEmail() throws Exception {
        LoginDTO loginDTO = new LoginDTO("emmox55@gmail.com");
        userSearchByEmail(loginDTO, "$.message", Messages.SUCCESS.getMessage(), status().isOk());
    }

    @Test
    @DisplayName("To Test a failed Login process using unregistered email")
    void testFindUserByEmail_Fail_1() throws Exception {
        LoginDTO loginDTO = new LoginDTO("embox55@gmail.com");
        userSearchByEmail(loginDTO, "$.message", Messages.NO_USER_RECORD_FOUND.getMessage(), status().isNotFound());
    }

    @Test
    @DisplayName("To Test a failed Login process using invalid inputs")
    void testFindUserByEmail_Fail_2() throws Exception {
        LoginDTO loginDTO = new LoginDTO("embox55gmail.com");
        userSearchByEmail(loginDTO, "$.message", Messages.VALIDATION_ERRORS.getMessage(), status().isBadRequest());
    }

    @Test
    @DisplayName("To Test a failed Login process using a wrong request Method")
    void testFindUserByEmail_Fail_3() throws Exception {
        LoginDTO loginDTO = new LoginDTO("embox55gmail.com");
        mockMvc.perform(put(URL_BASE + "/search?email=" + loginDTO.getEmail())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Is.is(METHOD_NOT_SUPPORTED.getMessage() + " GET")))
                .andDo(print());
    }

    private void userSignUp(UserDTO userDTO, String jsonPath, String jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(post(URL_BASE + "/signup")
                        .contentType(APPLICATION_JSON).content(asJsonString(userDTO)))
                .andExpect(expectedStatus)
                .andExpect(jsonPath(jsonPath, Is.is(jsonPathValue)))
                .andDo(print());
    }


    private void userSearchByEmail(LoginDTO loginDTO, String jsonPath, String jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(get(URL_BASE + "/search?email=" + loginDTO.getEmail())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(expectedStatus)
                .andExpect(jsonPath(jsonPath, Is.is(jsonPathValue)));

    }


}