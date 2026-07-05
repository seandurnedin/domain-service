package com.libraryapp.domain.entity;

import com.libraryapp.domain.enums.EBorrowingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "borrowing_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"user", "book"})
public class BorrowingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EBorrowingStatus status = EBorrowingStatus.ON_LOAN;

    @Column(name = "late_fee", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal lateFee = BigDecimal.ZERO.setScale(2);

    @PrePersist
    protected void prePersist() {
        if (status == null) status = EBorrowingStatus.ON_LOAN;
        if (lateFee == null) lateFee = BigDecimal.ZERO.setScale(2);
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getBookId() {
        return book != null ? book.getId() : null;
    }
}
