package io.core.libra.dao;

import io.core.libra.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByBookISBNCode(String bookISBNCode);

    @Query("SELECT b from Book b WHERE b.quantity > 0")
    List<Book> findAllAvailableBooks(Pageable pageable);


}
