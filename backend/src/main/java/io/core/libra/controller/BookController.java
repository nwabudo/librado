package io.core.libra.controller;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BookDTO;
import io.core.libra.dtos.BorrowDTO;
import io.core.libra.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/books")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

    private final BookService bookService;

    @GetMapping( "")
    public ResponseEntity<ApiResponse<?>> getAllBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        List<BookDTO> books = this.bookService.getBooks(page, size);
        HttpStatus status = books.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(new ApiResponse<>(books), status);
    }

    @PutMapping("/borrow")
    public ResponseEntity<ApiResponse<String>> borrowBook(@Valid @RequestBody BorrowDTO borrowDTO){
        return new ResponseEntity<>(bookService.borrowBook(borrowDTO), HttpStatus.OK);
    }

    @PutMapping("/return")
    public ResponseEntity<ApiResponse<String>> returnBook(@Valid @RequestBody BorrowDTO borrowDTO){
        return new ResponseEntity<>(bookService.returnBook(borrowDTO), HttpStatus.OK);
    }

    @GetMapping("/borrowed")
    public ResponseEntity<ApiResponse<?>> getUsersBorrowedBooks(
            @RequestParam("userId") Long userId) {
        List<BookDTO> books = this.bookService.getUsersBorrowedBooks(userId);
        HttpStatus status = books.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(new ApiResponse<>(books), status);
    }
}
