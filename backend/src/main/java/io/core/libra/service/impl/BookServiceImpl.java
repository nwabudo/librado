package io.core.libra.service.impl;

import io.core.libra.dao.BookRepository;
import io.core.libra.dao.PropertyRepository;
import io.core.libra.dao.UserRepository;
import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.BookDTO;
import io.core.libra.dtos.BorrowDTO;
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

import java.util.*;
import java.util.stream.Collectors;

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
    public List<BookDTO> getBooks(int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            List<Book> books = this.bookRepository.findAllAvailableBooks(pageable);
            return toCollectionDTO(books);
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public ApiResponse<String> borrowBook(BorrowDTO borrowDTO) {
        try {
            Book book = bookRepository.findByBookISBNCode(borrowDTO.getIsbnCode()).orElseThrow(() ->
                    new UserServiceException(Messages.NO_BOOK_RECORD_FOUND.getMessage()));

            // Condition to check if the book qty is greater than zero
            if(book.getQuantity() == 0){
                return new ApiResponse<>(Messages.BOOK_QUANTITY_DEPLETED.getMessage(), false);
            }

            User user = userRepository.findById(borrowDTO.getUserId()).orElseThrow(() ->
                    new UserServiceException(Messages.NO_USER_RECORD_FOUND.getMessage()));

            Property property = propertyRepository.findByPropertyCode(USER_LIMIT_CODE);
            int userBookLimit = property == null ? DEFAULT_USER_LIMIT: extractIntegerValue(property);
            Set<Book> userBooks = user.getBooks();

            // Condition to check if the user does not have the book
            // in his collection and has not exceeded the limit
            if(!userBooks.contains(book) && userBooks.size() < userBookLimit){
                int newQty = book.getQuantity() - 1;
                book.setQuantity(newQty);
                user.addBook(book);

                // Update entity
                userRepository.save(user);
                return new ApiResponse<>(Messages.SUCCESS_BORROWING_BOOK.getMessage(), true);
            }
            return new ApiResponse<>(Messages.BOOK_RECORD_ALREADY_EXISTS.getMessage(), false);
        } catch (UserServiceException ex){
            throw new UserServiceException(ex.getMessage());
        } catch (Exception ex){
            log.error(ex.getMessage());
            return new ApiResponse<>(Messages.PROBLEM_BORROWING_BOOK.getMessage(), false);
        }
    }

    @Override
    public ApiResponse<String> returnBook(BorrowDTO borrowDTO) {
        try {
            User user = userRepository.findById(borrowDTO.getUserId()).orElseThrow(() ->
                    new UserServiceException(Messages.NO_USER_RECORD_FOUND.getMessage()));
            Book book = bookRepository.findByBookISBNCode(borrowDTO.getIsbnCode()).orElseThrow(() ->
                    new UserServiceException(Messages.NO_BOOK_RECORD_FOUND.getMessage()));

            Set<Book> userBooks = user.getBooks();
            // Condition to check if the user has the book in his collection
            if(userBooks.contains(book)){
                int newQty = book.getQuantity() + 1;
                book.setQuantity(newQty);
                user.removeBook(book);
                // Update entity
                userRepository.save(user);
                bookRepository.save(book);
                return new ApiResponse<>(Messages.SUCCESS_RETURNING_BOOK.getMessage(), true);
            }
            return new ApiResponse<>(Messages.BOOK_RECORD_DOES_NOT_EXISTS.getMessage(), false);
        } catch (UserServiceException ex){
            return new ApiResponse<>(ex.getMessage(), false);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return new ApiResponse<>(Messages.PROBLEM_BORROWING_BOOK.getMessage(), false);
        }
    }

    @Override
    public List<BookDTO> getUsersBorrowedBooks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserServiceException(Messages.NO_USER_RECORD_FOUND.getMessage()));
        List<Book> books = new ArrayList<>(user.getBooks());
        return toCollectionDTO(books);
    }

    private BookDTO toModel(Book entity) {
        if(entity == null) return null;

        BookDTO model = new BookDTO();
        model.setBookImageUrl(entity.getBookImageUrl());
        model.setAuthorName(entity.getAuthorName());
        model.setBookTitle(entity.getBookTitle());
        model.setBookISBNCode(entity.getBookISBNCode());
        model.setQuantity(entity.getQuantity());
        return model;
    }

    private int extractIntegerValue(Property property){
        try{
            return Integer.valueOf(property.getPropertyValue());
        }catch(Exception ex){
            log.error("Value cannot be Converted to Integer");
        }
        return DEFAULT_USER_LIMIT;
    }

    private List<BookDTO> toCollectionDTO(List<Book> books) {
        return books.stream()
                .map(this::toModel)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
