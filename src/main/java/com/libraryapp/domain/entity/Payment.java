package com.libraryapp.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.libraryapp.domain.enums.EPaymentStatus;
import com.libraryapp.domain.enums.EPaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"user", "borrowingRecord"})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowing_record_id")
    private BorrowingRecord borrowingRecord;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EPaymentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EPaymentStatus status = EPaymentStatus.PENDING;

    @Column(name = "payment_date")
    @Builder.Default
    private LocalDateTime paymentDate = LocalDateTime.now();

    @PrePersist
    protected void prePersist() {
        if (status == null) status = EPaymentStatus.PENDING;
        if (paymentDate == null) paymentDate = LocalDateTime.now();
    }

    @JsonProperty("userId")
    public Long getUserRefId() {
        return user != null ? user.getId() : null;
    }

    public Long getBorrowingRecordId() {
        return borrowingRecord != null ? borrowingRecord.getId() : null;
    }
}
