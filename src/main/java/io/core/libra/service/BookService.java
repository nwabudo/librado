package io.core.libra.service;

import io.core.libra.entity.Book;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookService {

    @Query("SELECT b from Book b WHERE b.quantity > 0")
    List<Book> findAllAvailableBooks();
}
