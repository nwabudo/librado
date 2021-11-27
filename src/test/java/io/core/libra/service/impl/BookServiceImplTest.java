package io.core.libra.service.impl;

import io.core.libra.entity.Book;
import io.core.libra.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookServiceImplTest extends BaseTest {

    @Autowired
    private BookService bookService;

    private final String MESSAGE = "Expectations: %s || Actual: %s";

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
}