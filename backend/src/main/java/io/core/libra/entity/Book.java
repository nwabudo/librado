package io.core.libra.entity;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded= true, callSuper = false)
@ToString(exclude = {"users"})
public class Book extends AuditModel {

    @Column(length = 100)
    private String bookTitle;

    @Column(length = 50)
    private String authorName;

    private String bookImageUrl;

    @Column(nullable = false, unique = true)
    @NaturalId
    @EqualsAndHashCode.Include
    private String bookISBNCode;

    private Integer quantity;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "books")
    private Set<User> users = new HashSet<>();

    public Book(String bookISBNCode) {
        this.bookISBNCode = bookISBNCode;
    }
}
