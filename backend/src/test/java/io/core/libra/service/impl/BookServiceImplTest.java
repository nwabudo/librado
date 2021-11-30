package io.core.libra.service.impl;

import io.core.libra.BaseTest;
import io.core.libra.dao.BookRepository;
import io.core.libra.dao.UserRepository;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BookDTO;
import io.core.libra.dtos.BorrowDTO;
import io.core.libra.entity.Book;
import io.core.libra.entity.User;
import io.core.libra.exception.Messages;
import io.core.libra.service.BookService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void find_all_available_books() {
        int expectedSize = 5;

        List<BookDTO> page = bookService.getBooks(0, expectedSize);
        int actualSize = page.size();
        assertEquals(expectedSize, actualSize,
                String.format(MESSAGE, expectedSize, actualSize));
    }

    @Test
    void borrow_book_and_confirm_count_from_db_afterwards() {
        BorrowDTO borrowDTO = new BorrowDTO("ISBN2309872JT", 1L);
        User user = userRepository.findById(borrowDTO.getUserId()).orElse(new User());
        Book book = bookRepository.findByBookISBNCode(borrowDTO.getIsbnCode()).orElse(new Book());

        ApiResponse<String> actualResponse = bookService.borrowBook(borrowDTO);

        // We expect that the no of books for the user increments and the quantity of
        // book left in library decrement by 1 each after borrowing
        int expectedBookSize = user.getBooks().size() + 1;
        int expectedBookQty = book.getQuantity() - 1;

        assertEquals(expectedSuccessResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedSuccessResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedSuccessResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedSuccessResponse.getStatus(), actualResponse.getStatus()));

        user = userRepository.findById(borrowDTO.getUserId()).orElse(new User());
        book = bookRepository.findByBookISBNCode(borrowDTO.getIsbnCode()).orElse(new Book());

        assertEquals(expectedBookSize, user.getBooks().size(),
                String.format(MESSAGE, expectedBookSize, user.getBooks().size()));
        assertEquals(expectedBookQty, book.getQuantity(),
                String.format(MESSAGE, expectedBookQty, book.getQuantity()));
    }

    @Test
    @DisplayName("Borrow Book fail because it exists in users catalogue")
    void borrow_book_fail_as_user_already_borrowed_it() {

        BorrowDTO borrowDTO = new BorrowDTO("ISBN2309872JT", 1L);
        //Borrow the first time
        bookService.borrowBook(borrowDTO);
        //Borrow again
        ApiResponse<String> actualResponse = bookService.borrowBook(borrowDTO);

        assertEquals(expectedFailureResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedFailureResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedFailureResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedFailureResponse.getStatus(), actualResponse.getStatus()));
    }

    @Test
    @DisplayName("Borrow Book fail because user limit reached")
    void borrow_book_fail_because_user_limit_reached() {
        //Borrow the first time
        ApiResponse<String> actualResponse = bookService.borrowBook(new BorrowDTO("ISBN2309872JT", 1L));
        assertEquals(expectedSuccessResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedSuccessResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedSuccessResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedSuccessResponse.getStatus(), actualResponse.getStatus()));

        // Borrow Second
        actualResponse = bookService.borrowBook(new BorrowDTO("ISBN345872JA", 1L));
        assertEquals(expectedSuccessResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedSuccessResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedSuccessResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedSuccessResponse.getStatus(), actualResponse.getStatus()));

        // Try Borrowing the Third Time
        actualResponse = bookService.borrowBook(new BorrowDTO("ISBN56O3O22JA", 1L));
        assertEquals(expectedFailureResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedFailureResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedFailureResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedFailureResponse.getStatus(), actualResponse.getStatus()));
    }

    @Test
    void return_borrowed_book_successfully() {
        ApiResponse<String> expectedResponse = new ApiResponse<>(Messages.SUCCESS_RETURNING_BOOK.getMessage(), true);
        BorrowDTO borrowDTO = new BorrowDTO("ISBN2309872JT", 1L);

        //Borrow the Book ISBN2309872JT
        ApiResponse<String> actualResponse = bookService.borrowBook(borrowDTO);
        assertEquals(expectedSuccessResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedSuccessResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedSuccessResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedSuccessResponse.getStatus(), actualResponse.getStatus()));


        User user = userRepository.findById(borrowDTO.getUserId()).orElse(new User());
        Book book = bookRepository.findByBookISBNCode(borrowDTO.getIsbnCode()).orElse(new Book());

        // Return the book
        actualResponse = bookService.returnBook(borrowDTO);

        int expectedBookSize = user.getBooks().size() - 1;
        int expectedBookQty = book.getQuantity() + 1;

        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedResponse.getStatus(), actualResponse.getStatus()));

        user = userRepository.findById(borrowDTO.getUserId()).orElse(new User());
        book = bookRepository.findByBookISBNCode(borrowDTO.getIsbnCode()).orElse(new Book());

        assertEquals(expectedBookSize, user.getBooks().size(),
                String.format(MESSAGE, expectedBookSize, user.getBooks().size()));
        assertEquals(expectedBookQty, book.getQuantity(),
                String.format(MESSAGE, expectedBookQty, book.getQuantity()));
    }

    @Test
    @DisplayName("Return Book Fail because it no longer exists on User's Catalogue")
    void fail_on_returning_book_not_on_users_list() {
        ApiResponse<String> expectedResponse = new ApiResponse<>(Messages.BOOK_RECORD_DOES_NOT_EXISTS.getMessage(), false);
        BorrowDTO borrowDTO = new BorrowDTO("ISBN2309872JT", 1L);

        //Borrow the Book ISBN2309872JT
        ApiResponse<String> actualResponse = bookService.borrowBook(borrowDTO);
        assertEquals(expectedSuccessResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedSuccessResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedSuccessResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedSuccessResponse.getStatus(), actualResponse.getStatus()));

        // Return the book - First Time
        actualResponse = bookService.returnBook(borrowDTO);
        assertEquals(Messages.SUCCESS_RETURNING_BOOK.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, Messages.SUCCESS_RETURNING_BOOK.getMessage(), actualResponse.getMessage()));
        assertEquals(true, actualResponse.getStatus(),
                String.format(MESSAGE, true, actualResponse.getStatus()));

        // Try returning again
        actualResponse = bookService.returnBook(borrowDTO);
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedResponse.getStatus(), actualResponse.getStatus()));
    }

    @Test
    @DisplayName("Get all Books borrowed by User")
    void fetch_all_books_borrowed_by_user() {
        bookService.borrowBook(new BorrowDTO("ISBN345872JA", 1L));

        int expectedSize = 1;
        List<BookDTO> books = bookService.getUsersBorrowedBooks(1L);

        assertEquals(expectedSize, books.size(),
                String.format(MESSAGE, expectedSize, books.size()));
        assertEquals("ISBN345872JA", books.get(0).getBookISBNCode());
    }
}