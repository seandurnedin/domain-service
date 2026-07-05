package com.libraryapp.domain.entity;

import com.libraryapp.domain.enums.EBookStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_books_isbn", columnList = "isbn", unique = true)})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 14)
    private String isbn;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 150)
    private String author;

    @Column(length = 150)
    private String publisher;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(length = 80)
    private String genre;

    @Column(name = "total_copies", nullable = false)
    @Builder.Default
    private Integer totalCopies = 1;

    @Column(name = "available_copies", nullable = false)
    @Builder.Default
    private Integer availableCopies = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EBookStatus status = EBookStatus.IN_STORE;

    @PrePersist
    protected void prePersist() {
        if (totalCopies == null) totalCopies = 1;
        if (availableCopies == null) availableCopies = totalCopies;
        if (status == null) status = EBookStatus.IN_STORE;
    }
}
