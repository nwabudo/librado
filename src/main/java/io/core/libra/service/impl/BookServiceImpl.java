package io.core.libra.service.impl;

import io.core.libra.dao.BookRepository;
import io.core.libra.entity.Book;
import io.core.libra.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Optional<Book> findBookByISBNCode(String isbnCode) {
        return Optional.empty();
    }

    @Override
    public List<Book> getBooks(int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            return this.bookRepository.findAllAvailableBooks(pageable);
        }catch(Exception ex){
            log.error("An Error has Occurred: {}", ex.getMessage());
        }
        return Collections.emptyList();
    }
}
