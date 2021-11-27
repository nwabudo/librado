package io.core.libra.service.impl;

import io.core.libra.dao.BookRepository;
import io.core.libra.dao.PropertyRepository;
import io.core.libra.dao.UserRepository;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BorrowModel;
import io.core.libra.entity.Book;
import io.core.libra.entity.Property;
import io.core.libra.entity.User;
import io.core.libra.exception.Messages;
import io.core.libra.exception.UserServiceException;
import io.core.libra.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    private final String USER_LIMIT_CODE = "user.book.limit";
    private final int DEFAULT_USER_LIMIT = 2;

    @Override
    public Optional<Book> findBookByISBNCode(String isbnCode) {
        try{
            return bookRepository.findByBookISBNCode(isbnCode);
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Book> getBooks(int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            return this.bookRepository.findAllAvailableBooks(pageable);
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public ApiResponse<String> borrowBook(BorrowModel borrowModel) {
        try {
            User user = userRepository.findById(borrowModel.getUserId()).orElseThrow(() ->
                    new UserServiceException(Messages.NO_USER_RECORD_FOUND.getErrorMessage()));
            Book book = bookRepository.findByBookISBNCode(borrowModel.getIsbnCode()).orElseThrow(() ->
                    new UserServiceException(Messages.NO_BOOK_RECORD_FOUND.getErrorMessage()));
            Property property = propertyRepository.findByPropertyCode(USER_LIMIT_CODE);

            int userBookLimit = property == null ? DEFAULT_USER_LIMIT: extractIntegerValue(property);

            Set<Book> userBooks = user.getBooks();
            if(!user.getBooks().contains(book) && userBooks.size() < userBookLimit){
                user.addBook(book);
                userRepository.save(user);
                return new ApiResponse<>(Messages.SUCCESS_BORROWING_BOOK.getErrorMessage(), true);
            }
            return new ApiResponse<>(Messages.BOOK_RECORD_ALREADY_EXISTS.getErrorMessage(), false);
        } catch (UserServiceException ex){
            return new ApiResponse<>(ex.getMessage(), false);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return new ApiResponse<>(Messages.PROBLEM_BORROWING_BOOK.getErrorMessage(), false);
        }
    }

    private int extractIntegerValue(Property property){
        try{
            return Integer.valueOf(property.getPropertyValue());
        }catch(Exception ex){
            log.error("Value cannot be Converted to Integer");
        }
        return DEFAULT_USER_LIMIT;
    }
}
