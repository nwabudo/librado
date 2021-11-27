package io.core.libra.controller;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BookDTO;
import io.core.libra.dtos.BorrowModel;
import io.core.libra.dtos.assembler.BookAssembler;
import io.core.libra.entity.Book;
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
@RequestMapping("api/v1/library")
public class LibraryController {

    private final BookService bookService;
    private final BookAssembler bookAssembler;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = { "application/hal+json" })
    public ResponseEntity<CollectionModel<BookDTO>> getAllBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        List<Book> books = this.bookService.getBooks(page, size);
        return new ResponseEntity<>(
                bookAssembler.toCollectionModel(books),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{isbnCode}", method = RequestMethod.GET, produces = { "application/hal+json" })
    public ResponseEntity<BookDTO> getByBookISBNCode(@PathVariable("isbnCode") String bookISBNCode) {
        return bookService.findBookByISBNCode(bookISBNCode)
                .map(bookAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/borrow", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<String>> borrowBook(@Valid @RequestBody BorrowModel borrowModel){
        return new ResponseEntity<>(bookService.borrowBook(borrowModel), HttpStatus.OK);
    }
}
