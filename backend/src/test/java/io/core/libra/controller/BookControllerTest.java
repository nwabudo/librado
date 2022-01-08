package io.core.libra.controller;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BorrowDTO;
import io.core.libra.exception.Messages;
import io.core.libra.exception.UserServiceException;
import io.core.libra.service.BookService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static io.core.libra.HelperClass.asJsonString;
import static io.core.libra.HelperClass.formBookDTO;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@ActiveProfiles("test")
class BookControllerTest {

    private final String URL_BASE = "/api/v1/books";
    private final String ISBN_CODE = "ISBN2340987";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void get_all_books() throws Exception {
        getAllBook(0, 5, "$.message", Messages.SUCCESS.getMessage(), status().isOk());
    }

    @Test
    void borrow_book_and_pass() throws Exception {
        BorrowDTO borrowDTO = new BorrowDTO(ISBN_CODE, 1L);
        borrowBook(borrowDTO, "$.message", Messages.SUCCESS_BORROWING_BOOK.getMessage(), status().isOk());
    }

    @Test
    void return_book_and_pass() throws Exception {
        BorrowDTO borrowDTO = new BorrowDTO(ISBN_CODE, 1L);

        borrowBook(borrowDTO, "$.message", Messages.SUCCESS_BORROWING_BOOK.getMessage(), status().isOk());

        returnBook(borrowDTO, "$.message", Messages.SUCCESS_RETURNING_BOOK.getMessage(), status().isOk());
    }

    @Test
    @DisplayName("Get Borrowed Books of a valid User")
    void pass_when_getting_books_of_a_valid_user() throws Exception {
        BorrowDTO borrowDTO = new BorrowDTO(ISBN_CODE, 1L);
        borrowBook(borrowDTO, "$.message", Messages.SUCCESS_BORROWING_BOOK.getMessage(), status().isOk());

        when(bookService.getUsersBorrowedBooks(anyLong())).thenReturn(List.of(formBookDTO(ISBN_CODE)));

        getAllBorrowedBook(1L, "$.message", Messages.SUCCESS.getMessage(), status().isOk());
    }

    @Test
    @DisplayName("Get Borrowed Books of an invalid User")
    void fail_when_getting_borrowed_books_of_invalid_user() throws Exception {
        when(bookService.getUsersBorrowedBooks(anyLong()))
                .thenThrow(new UserServiceException(Messages.NO_USER_RECORD_FOUND.getMessage()));

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
        when(bookService.getBooks(anyInt(), anyInt())).thenReturn(List.of(formBookDTO(ISBN_CODE)));

        mockMvc.perform(get(URL_BASE + "?page=" + page + "&size=" + size)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(expectedStatus)
                .andExpect(jsonPath(jsonPath, Is.is(jsonPathValue)));
    }

    private void borrowBook(BorrowDTO borrowDTO, String jsonPath, String jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        ApiResponse resp = new ApiResponse<>(Messages.SUCCESS_BORROWING_BOOK.getMessage());
        when(bookService.borrowBook(any())).thenReturn(resp);

        mockMvc.perform(put(URL_BASE + "/borrow")
                        .contentType(APPLICATION_JSON).content(asJsonString(borrowDTO)))
                .andExpect(expectedStatus)
                .andExpect(jsonPath(jsonPath, Is.is(jsonPathValue)))
                .andDo(print());
    }

    private void returnBook(BorrowDTO borrowDTO, String jsonPath, String jsonPathValue, ResultMatcher expectedStatus) throws Exception {
        ApiResponse resp = new ApiResponse<>(Messages.SUCCESS_RETURNING_BOOK.getMessage());
        when(bookService.returnBook(any())).thenReturn(resp);

        mockMvc.perform(put(URL_BASE + "/return")
                        .contentType(APPLICATION_JSON).content(asJsonString(borrowDTO)))
                .andExpect(expectedStatus)
                .andExpect(jsonPath(jsonPath, Is.is(jsonPathValue)))
                .andDo(print());
    }

}