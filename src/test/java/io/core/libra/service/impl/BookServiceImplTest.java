package io.core.libra.service.impl;

import io.core.libra.dao.UserRepository;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BorrowModel;
import io.core.libra.entity.Book;
import io.core.libra.entity.User;
import io.core.libra.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class BookServiceImplTest extends BaseTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private UserRepository userRepository;

    private final String MESSAGE = "Expectations: %s || Actual: %s";
    private User user = new User("Emmanuel Nwabudo", "nwabudoemmanuel@gmail.com");

    @BeforeEach
    void setUp() {
    }


    @Test
    void findBookByISBNCode() {
        String expectedISBN = "ISBN2309872JT";
        Book book = bookService.findBookByISBNCode(expectedISBN).orElse(new Book());
        assertEquals(expectedISBN, book.getBookISBNCode(), String.format(MESSAGE, expectedISBN, book.getBookISBNCode()));
    }

    @Test
    void findBookByISBNCode_Fail() {
        String expectedISBN = "WrongISBNCode";
        Book book = bookService.findBookByISBNCode(expectedISBN).orElse(new Book());
        assertNotEquals(expectedISBN, book.getBookISBNCode(), String.format(MESSAGE, expectedISBN, book.getBookISBNCode()));
    }

    @Test
    void getBooks() {
        int expectedSize = 5;
        List<Book> page = bookService.getBooks(0, expectedSize);
        int actualSize = page.size();
        assertEquals(expectedSize, actualSize,
                String.format(MESSAGE, expectedSize, actualSize));
    }

    @Test
    void borrowBook() {
        doReturn(Optional.of(user)).when(userRepository).findById(any());
        doReturn(user).when(userRepository).save(any());
        ApiResponse<String> expectedResponse = new ApiResponse<>("Success!! Book added to User", true);

        ApiResponse<String> actualResponse = bookService.borrowBook(new BorrowModel("ISBN2309872JT", 1L));
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedResponse.getStatus(), actualResponse.getStatus()));
    }

}