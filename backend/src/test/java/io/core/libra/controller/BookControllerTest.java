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

    private final String URL_BASE = "/api/v1/book";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllBooks() throws Exception {
        getAllBook(0, 5, "$.message", Messages.SUCCESS.getMessage(), status().isOk());
    }

    @DisplayName("Find a Book by valid ISBN Code")
    @Test
    void getByBookISBNCode() throws Exception {
        getBookByISBNCode("ISBN345872JA", "$.data.bookTitle", "Building APIs: Know How", status().isOk());
    }

    @DisplayName("Find a Book by invalid ISBN Code")
    @Test
    void getByBookISBNCodeFail() throws Exception {
        getBookByISBNCode("0012ISBN345872JA", "$.message", Messages.NO_BOOK_RECORD_FOUND.getMessage(), status().isNotFound());
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
        getBookByISBNCode(borrowDTO.getIsbnCode(), "$.data.quantity", 9, status().isOk());

        returnBook(borrowDTO, "$.message", Messages.SUCCESS_RETURNING_BOOK.getMessage(), status().isOk());
        getBookByISBNCode(borrowDTO.getIsbnCode(), "$.data.quantity", 10, status().isOk());
    }

    @Test
    @DisplayName("Get Borrowed Books of a valid User")
    void pass_when_getting_books_of_a_valid_user() throws Exception {
        getAllBorrowedBook(1L, "$.message", Messages.SUCCESS.getMessage(), status().isOk());
    }

    @Test
    @DisplayName("Get Borrowed Books of an invalid User")
    void fail_when_getting_borrowed_books_of_invalid_user() throws Exception {
        getAllBorrowedBook(200L, "$.message", Messages.NO_USER_RECORD_FOUND.getMessage(), status().isBadRequest());
    }

    private void getAllBorrowedBook(long userId, String jsonPath, String jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(get(URL_BASE + "/borrow/" + userId)
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

    private void getBookByISBNCode(String isbnCode, String jsonPath, Object jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(get(URL_BASE + "/" + isbnCode)
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