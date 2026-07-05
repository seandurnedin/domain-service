package com.libraryapp.domain.entity;

import com.libraryapp.domain.enums.EReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"user", "book"})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "reservation_date", nullable = false)
    @Builder.Default
    private LocalDateTime reservationDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EReservationStatus status = EReservationStatus.RESERVED;

    @Column(name = "notified_at")
    private LocalDateTime notifiedAt;

    /**
     * Position in the queue for that book at the time of reservation (1 = next in line).
     */
    @Column(name = "queue_position")
    private Integer queuePosition;

    @PrePersist
    protected void prePersist() {
        if (reservationDate == null) reservationDate = LocalDateTime.now();
        if (status == null) status = EReservationStatus.RESERVED;
    }
}
