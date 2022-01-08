package io.core.libra.service.impl;

import io.core.libra.dao.BookRepository;
import io.core.libra.dao.PropertyRepository;
import io.core.libra.dao.UserRepository;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BookDTO;
import io.core.libra.dtos.BorrowDTO;
import io.core.libra.entity.Book;
import io.core.libra.entity.Property;
import io.core.libra.entity.User;
import io.core.libra.exception.Messages;
import io.core.libra.service.BookService;
import org.hibernate.validator.constraints.ISBN;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static io.core.libra.HelperClass.formBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private BookRepository bookRepository;

    private final String ISBN_CODE = "ISBN2340987";
    private final String USER_LIMIT_CODE = "user.book.limit";
    private final String USER_LIMIT = "2";

    private final String MESSAGE = "Expectations: %s || Actual: %s";

    Book book = formBook(ISBN_CODE, 20);
    User user = new User("emmanuel", "nwabudo", "emmox@hexad.com");
    Property property = new Property(USER_LIMIT_CODE, USER_LIMIT);

    @BeforeEach
    public void setup(){
        lenient().when(bookRepository.findByBookISBNCode(any())).thenReturn(Optional.of(book));
        lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(any())).thenReturn(user);
        lenient().when(bookRepository.save(any())).thenReturn(book);
        lenient().when(propertyRepository.findByPropertyCode(any())).thenReturn(property);
    }

    @Test
    void find_all_available_books() {
        List<Book> resp = List.of(formBook(ISBN_CODE, 20));
        int expectedSize = resp.size();
        when(bookRepository.findAllAvailableBooks(any())).thenReturn(resp);

        List<BookDTO> page = bookService.getBooks(0, expectedSize);
        int actualSize = page.size();
        assertEquals(expectedSize, actualSize,
                String.format(MESSAGE, expectedSize, actualSize));
    }

    @Test
    void borrow_book_and_pass() {
        BorrowDTO borrowDTO = new BorrowDTO(ISBN_CODE, 1L);

        ApiResponse<String> actualResponse = bookService.borrowBook(borrowDTO);

        assertEquals(Messages.SUCCESS_BORROWING_BOOK.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, Messages.SUCCESS_BORROWING_BOOK.getMessage(), actualResponse.getMessage()));
        assertEquals(true, actualResponse.getStatus(),
                String.format(MESSAGE, true, actualResponse.getStatus()));
    }

    @Test
    void borrow_book_fail_as_book_qty_depleted() {
        // Set bok quantity to zero
        book.setQuantity(0);

        BorrowDTO borrowDTO = new BorrowDTO(ISBN_CODE, 1L);

        ApiResponse<String> actualResponse = bookService.borrowBook(borrowDTO);

        assertEquals(Messages.BOOK_QUANTITY_DEPLETED.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, Messages.BOOK_QUANTITY_DEPLETED.getMessage(), actualResponse.getMessage()));
        assertEquals(false, actualResponse.getStatus(),
                String.format(MESSAGE, false, actualResponse.getStatus()));
    }

    @Test
    @DisplayName("Borrow Book fail because it exists in users catalogue")
    void borrow_book_fail_as_user_already_borrowed_it() {
        //Borrow the first time
        user.addBook(book);

        BorrowDTO borrowDTO = new BorrowDTO(ISBN_CODE, 1L);

        //Borrow again
        ApiResponse<String> actualResponse = bookService.borrowBook(borrowDTO);

        assertEquals(Messages.BOOK_RECORD_ALREADY_EXISTS.getMessage(), actualResponse.getMessage());
        assertEquals(false, actualResponse.getStatus());
    }

    @Test
    @DisplayName("Borrow Book fail because user limit reached")
    void borrow_book_fail_because_user_limit_reached() {
        //Borrow the first time
        user.addBook(formBook("ISBN56O3O22JA", 50));

        // Borrow Second
        user.addBook(formBook("ISBN345872JA", 50));

        // Try Borrowing the Third Time
        ApiResponse<String> actualResponse = bookService.borrowBook(new BorrowDTO(ISBN_CODE, 1L));

        assertEquals(Messages.BOOK_RECORD_ALREADY_EXISTS.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, Messages.BOOK_RECORD_ALREADY_EXISTS.getMessage(), actualResponse.getMessage()));
        assertEquals(false, actualResponse.getStatus(),
                String.format(MESSAGE, false, actualResponse.getStatus()));
    }

    @Test
    void return_borrowed_book_successfully() {
        ApiResponse<String> expectedResponse = new ApiResponse<>(Messages.SUCCESS_RETURNING_BOOK.getMessage(), true);

        user.addBook(book);

        // Return the book
        BorrowDTO borrowDTO = new BorrowDTO(ISBN_CODE, 1L);
        ApiResponse<String> actualResponse = bookService.returnBook(borrowDTO);

        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage(),
                String.format(MESSAGE, expectedResponse.getMessage(), actualResponse.getMessage()));
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus(),
                String.format(MESSAGE, expectedResponse.getStatus(), actualResponse.getStatus()));
    }

    @Test
    @DisplayName("Return Book Fail because it no longer exists on User's Catalogue")
    void fail_on_returning_book_not_on_users_list() {
        ApiResponse<String> expectedResponse = new ApiResponse<>(Messages.BOOK_RECORD_DOES_NOT_EXISTS.getMessage(), false);
        BorrowDTO borrowDTO = new BorrowDTO(ISBN_CODE, 1L);

        //Borrow the Book
        user.addBook(book);

        ApiResponse<String> actualResponse = bookService.returnBook(borrowDTO);
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
        //Borrow the first time
        user.addBook(book);

        int expectedSize = user.getBooks().size();
        List<BookDTO> books = bookService.getUsersBorrowedBooks(1L);

        assertEquals(expectedSize, books.size(),
                String.format(MESSAGE, expectedSize, books.size()));
        assertEquals(book.getBookISBNCode(), books.get(0).getBookISBNCode());
    }
}