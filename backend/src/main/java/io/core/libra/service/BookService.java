package io.core.libra.service;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BookDTO;
import io.core.libra.dtos.BorrowDTO;

import java.util.List;

public interface BookService {

    List<BookDTO> getBooks(int page, int size);

    ApiResponse<String> borrowBook(BorrowDTO borrowDTO);

    ApiResponse<String> returnBook(BorrowDTO borrowDTO);

    List<BookDTO> getUsersBorrowedBooks(Long userId);
}
