package io.core.libra.controller;

import io.core.libra.BaseTest;
import io.core.libra.dtos.BorrowDTO;
import io.core.libra.exception.Messages;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static io.core.libra.JsonString.asJsonString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class BookControllerTest extends BaseTest {

    private final String URL_BASE = "/api/v1/books";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllBooks() throws Exception {
        getAllBook(0, 5, "$.message", Messages.SUCCESS.getMessage(), status().isOk());
    }

    @Test
    void borrowBook() throws Exception {
        BorrowDTO borrowDTO = new BorrowDTO("ISBN2309872JT", 1L);
        borrowBook(borrowDTO, "$.message", Messages.SUCCESS_BORROWING_BOOK.getMessage(), status().isOk());
    }

    @Test
    void returnBook() throws Exception {
        BorrowDTO borrowDTO = new BorrowDTO("ISBN3438092JO", 1L);

        borrowBook(borrowDTO, "$.message", Messages.SUCCESS_BORROWING_BOOK.getMessage(), status().isOk());

        returnBook(borrowDTO, "$.message", Messages.SUCCESS_RETURNING_BOOK.getMessage(), status().isOk());
    }

    @Test
    @DisplayName("Get Borrowed Books of a valid User")
    void pass_when_getting_books_of_a_valid_user() throws Exception {
        BorrowDTO borrowDTO = new BorrowDTO("ISBN3438092JO", 1L);
        borrowBook(borrowDTO, "$.message", Messages.SUCCESS_BORROWING_BOOK.getMessage(), status().isOk());

        getAllBorrowedBook(1L, "$.message", Messages.SUCCESS.getMessage(), status().isOk());
    }

    @Test
    @DisplayName("Get Borrowed Books of an invalid User")
    void fail_when_getting_borrowed_books_of_invalid_user() throws Exception {
        getAllBorrowedBook(200L, "$.message", Messages.NO_USER_RECORD_FOUND.getMessage(), status().isBadRequest());
    }

    private void getAllBorrowedBook(long userId, String jsonPath, String jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(get(URL_BASE + "/borrowed?userId=" + userId)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(expectedStatus)
                .andExpect(jsonPath(jsonPath, Is.is(jsonPathValue)));
    }

    private void getAllBook(int page, int size, String jsonPath, String jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(get(URL_BASE + "?page=" + page + "&size=" + size)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(expectedStatus)
                .andExpect(jsonPath(jsonPath, Is.is(jsonPathValue)));
    }

    private void borrowBook(BorrowDTO borrowDTO, String jsonPath, String jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(put(URL_BASE + "/borrow")
                        .contentType(APPLICATION_JSON).content(asJsonString(borrowDTO)))
                .andExpect(expectedStatus)
                .andExpect(jsonPath(jsonPath, Is.is(jsonPathValue)))
                .andDo(print());
    }

    private void returnBook(BorrowDTO borrowDTO, String jsonPath, String jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(put(URL_BASE + "/return")
                        .contentType(APPLICATION_JSON).content(asJsonString(borrowDTO)))
                .andExpect(expectedStatus)
                .andExpect(jsonPath(jsonPath, Is.is(jsonPathValue)))
                .andDo(print());
    }

}