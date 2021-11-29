package io.core.libra.dtos.assembler;

import io.core.libra.BaseTest;
import io.core.libra.dao.BookRepository;
import io.core.libra.dtos.BookDTO;
import io.core.libra.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookAssemblerTest extends BaseTest {

    @Autowired
    private BookAssembler bookAssembler;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testToMethod(){
        Book book = bookRepository.findByBookISBNCode("").orElse(null);
        BookDTO bookDTO = bookAssembler.toModel(book);

        assertEquals(book, bookDTO, "Test to ensure that same Data is passed");
    }

    @Test
    void testToCollectionModel(){
        Collection<Book> books = bookRepository.findAllAvailableBooks(Pageable.ofSize(5));
        CollectionModel<BookDTO> bookDTOS = bookAssembler.toCollectionModel(books);

        assertEquals(books.size(), bookDTOS.getContent().size(), "Test to ensure that same Data is passed");
    }

}