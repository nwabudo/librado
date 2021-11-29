package io.core.libra.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_user")
@Data
@NoArgsConstructor
@ToString(exclude = {"books"})
public class User extends AuditModel {

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "tbl_user_book",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.books = new HashSet<>();
    }

    public User(String email) {
        this.email = email;
    }

    public void addBook(Book book) {
        books.add(book);
        book.getUsers().add(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.getUsers().remove(this);
    }
}
