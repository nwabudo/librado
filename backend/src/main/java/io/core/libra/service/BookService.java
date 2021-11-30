package io.core.libra.service;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BorrowDTO;
import io.core.libra.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findBookByISBNCode(String isbnCode);

    List<Book> getBooks(int page, int size);

    ApiResponse<String> borrowBook(BorrowDTO borrowDTO);

    ApiResponse<String> returnBook(BorrowDTO borrowDTO);

    List<Book> getUsersBorrowedBooks(Long userId);
}
