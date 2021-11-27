package io.core.libra.service;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BorrowModel;
import io.core.libra.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findBookByISBNCode(String isbnCode);

    List<Book> getBooks(int page, int size);

    ApiResponse<String> borrowBook(BorrowModel borrowModel);

    ApiResponse<String> returnBook(BorrowModel borrowModel);
}
