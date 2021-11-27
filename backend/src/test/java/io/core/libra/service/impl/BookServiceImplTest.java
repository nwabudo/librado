package io.core.libra.service.impl;

import io.core.libra.BaseTest;
import io.core.libra.dao.BookRepository;
import io.core.libra.dao.UserRepository;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BorrowModel;
import io.core.libra.entity.Book;
import io.core.libra.entity.User;
import io.core.libra.exception.Messages;
import io.core.libra.service.BookService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceImplTest extends BaseTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private final String MESSAGE = "Expectations: %s || Actual: %s";
    ApiResponse<String> expectedSuccessResponse = new ApiResponse<>(Messages.SUCCESS_BORROWING_BOOK.getMessage(), true);
    ApiResponse<String> expectedFailureResponse = new ApiResponse<>(Messages.BOOK_RECORD_ALREADY_EXISTS.getMessage(), false);

    @Test
    @Order(1)
    void findBookByISBNCode() {
        String expectedISBN = "ISBN2309872JT";
        Book book = bookService.findBookByISBNCode(expectedISBN).orElse(new Book());
        assertEquals(expectedISBN, book.getBookISBNCode(), String.format(MESSAGE, expectedISBN, book.getBookISBNCode()));
    }

    @Test
    @Order(2)
    void findBookByISBNCode_Fail() {
        String expectedISBN = "WrongISBNCode";
        Book book = bookService.findBookByISBNCode(expectedISBN).orElse(new Book());
        assertNotEquals(expectedISBN, book.getBookISBNCode(), String.format(MESSAGE, expectedISBN, book.getBookISBNCode()));
    }

    @Test
    @Order(3)
    void getBooks() {
        int expectedSize = 5;
        List<Book> page = bookService.getBooks(0, expectedSize);
        int actualSize = page.size();
        assertEquals(expectedSize, actualSize,
                String.format(MESSAGE, expectedSize, actualSize));
    }

    @Test
    @Order(4)
    void borrowBook() {
        BorrowModel borrowModel = new BorrowModel("ISBN2309872JT", 1L);
        User user = userRepository.findById(borrowModel.getUserId()).orElse(new User());
        Book book = bookRepository.findByBookISBNCode(borrowModel.getIsbnCode()).orElse(new Book());

        ApiResponse<String> actualResponse = bookService.borrowBook(borrowModel);

        int expectedBookSize = user.getBooks().size() + 1;
        int expectedBookQty = book.getQuantity() - 1;

        assertEquals(expectedSuccessResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedSuccessResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedSuccessResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedSuccessResponse.getStatus(), actualResponse.getStatus()));

        user = userRepository.findById(borrowModel.getUserId()).orElse(new User());
        book = bookRepository.findByBookISBNCode(borrowModel.getIsbnCode()).orElse(new Book());

        assertEquals(expectedBookSize, user.getBooks().size(),
                String.format(MESSAGE, expectedBookSize, user.getBooks().size()));
        assertEquals(expectedBookQty, book.getQuantity(),
                String.format(MESSAGE, expectedBookQty, book.getQuantity()));
    }

    @Test
    @DisplayName("Borrow Book fail because it exists in users catalogue")
    @Order(5)
    void borrowBookFail() {

        BorrowModel borrowModel = new BorrowModel("ISBN2309872JT", 1L);

        ApiResponse<String> actualResponse = bookService.borrowBook(borrowModel);

        assertEquals(expectedFailureResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedFailureResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedFailureResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedFailureResponse.getStatus(), actualResponse.getStatus()));
    }

    @Test
    @DisplayName("Borrow Book fail because user limit reached")
    @Order(6)
    void borrowBookFailUserLimit() {

        // Borrow Second
        ApiResponse<String> actualResponse = bookService.borrowBook(new BorrowModel("ISBN345872JA", 1L));
        assertEquals(expectedSuccessResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedSuccessResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedSuccessResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedSuccessResponse.getStatus(), actualResponse.getStatus()));

        // Try Borrowing the Third Time
        actualResponse = bookService.borrowBook(new BorrowModel("ISBN56O3O22JA", 1L));
        assertEquals(expectedFailureResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedFailureResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedFailureResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedFailureResponse.getStatus(), actualResponse.getStatus()));
    }

    @Test
    @Order(6)
    void returnBook() {
        ApiResponse<String> expectedResponse = new ApiResponse<>(Messages.SUCCESS_RETURNING_BOOK.getMessage(), true);

        BorrowModel borrowModel = new BorrowModel("ISBN2309872JT", 1L);
        User user = userRepository.findById(borrowModel.getUserId()).orElse(new User());
        Book book = bookRepository.findByBookISBNCode(borrowModel.getIsbnCode()).orElse(new Book());

        ApiResponse<String> actualResponse = bookService.returnBook(borrowModel);

        int expectedBookSize = user.getBooks().size() - 1;
        int expectedBookQty = book.getQuantity() + 1;

        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedResponse.getStatus(), actualResponse.getStatus()));

        user = userRepository.findById(borrowModel.getUserId()).orElse(new User());
        book = bookRepository.findByBookISBNCode(borrowModel.getIsbnCode()).orElse(new Book());

        assertEquals(expectedBookSize, user.getBooks().size(),
                String.format(MESSAGE, expectedBookSize, user.getBooks().size()));
        assertEquals(expectedBookQty, book.getQuantity(),
                String.format(MESSAGE, expectedBookQty, book.getQuantity()));
    }

    @Test
    @DisplayName("Return Book Fail because it no longer exists on User's Catalogue")
    @Order(7)
    void returnBookFail() {
        ApiResponse<String> expectedResponse = new ApiResponse<>(Messages.BOOK_RECORD_DOES_NOT_EXISTS.getMessage(), false);

        BorrowModel borrowModel = new BorrowModel("ISBN2309872JT", 1L);

        ApiResponse<String> actualResponse = bookService.returnBook(borrowModel);

        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedResponse.getStatus(), actualResponse.getStatus()));
    }

}