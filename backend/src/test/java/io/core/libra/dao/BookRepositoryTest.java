package io.core.libra.dao;

import io.core.libra.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    void findByBookISBNCode() {
        String isbnCode = "ISBN7649827TA";
        Book book = bookRepository.findByBookISBNCode(isbnCode).orElse(null);
        assertThat(book).hasFieldOrPropertyWithValue("bookISBNCode", isbnCode);
    }

    @Test
    void findAllAvailableBooks() {
        String isbnCode = "ISBN7649827TA";
        Book book = bookRepository.findByBookISBNCode(isbnCode).orElse(new Book(isbnCode));

        // Modify the quantity and Save
        book.setQuantity(0);
        entityManager.persist(book);

        int size = bookRepository.findAll().size();
        assertThat(bookRepository.findAllAvailableBooks(Pageable.ofSize(size))).doesNotContain(book);
    }
}