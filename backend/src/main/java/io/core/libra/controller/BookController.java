package io.core.libra.controller;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BookDTO;
import io.core.libra.dtos.BorrowDTO;
import io.core.libra.dtos.assembler.BookAssembler;
import io.core.libra.entity.Book;
import io.core.libra.exception.Messages;
import io.core.libra.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/book")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

    private final BookService bookService;
    private final BookAssembler bookAssembler;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = { "application/hal+json" })
    public ResponseEntity<ApiResponse<CollectionModel<BookDTO>>> getAllBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        List<Book> books = this.bookService.getBooks(page, size);
        return new ResponseEntity<>(
                new ApiResponse<>(bookAssembler.toCollectionModel(books)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{isbnCode}", method = RequestMethod.GET, produces = { "application/hal+json" })
    public ResponseEntity<ApiResponse<BookDTO>> getByBookISBNCode(@PathVariable("isbnCode") String bookISBNCode) {
        Book book = bookService.findBookByISBNCode(bookISBNCode).orElse(null);
        ApiResponse<BookDTO> bookDTO = book != null ? new ApiResponse<>(bookAssembler.toModel(book))
                : new ApiResponse<>(Messages.NO_BOOK_RECORD_FOUND.getMessage(), false);
        HttpStatus status = !bookDTO.getStatus() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(bookDTO, status);
    }

    @RequestMapping(value = "/borrow", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<String>> borrowBook(@Valid @RequestBody BorrowDTO borrowDTO){
        return new ResponseEntity<>(bookService.borrowBook(borrowDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/return", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<String>> returnBook(@Valid @RequestBody BorrowDTO borrowDTO){
        return new ResponseEntity<>(bookService.returnBook(borrowDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/borrow/{userId}", method = RequestMethod.GET, produces = { "application/hal+json" })
    public ResponseEntity<ApiResponse<CollectionModel<BookDTO>>> getUsersBorrowedBooks(
            @PathVariable Long userId) {
        List<Book> books = this.bookService.getUsersBorrowedBooks(userId);
        return new ResponseEntity<>(
                new ApiResponse<>(bookAssembler.toCollectionModel(books)),
                HttpStatus.OK);
    }
}
