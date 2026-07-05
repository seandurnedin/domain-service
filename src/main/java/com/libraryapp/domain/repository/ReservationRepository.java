package com.libraryapp.domain.repository;

import com.libraryapp.domain.entity.Reservation;
import com.libraryapp.domain.enums.EReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "reservations", collectionResourceRel = "reservations")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @RestResource(path = "by-user", rel = "byUser")
    List<Reservation> findByUserId(@Param("userId") Long userId);

    @RestResource(path = "by-user-and-status", rel = "byUserAndStatus")
    List<Reservation> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") EReservationStatus status);

    @RestResource(path = "queue-for-book", rel = "queueForBook")
    List<Reservation> findByBookIdAndStatusOrderByReservationDateAsc(@Param("bookId") Long bookId, @Param("status") EReservationStatus status);

    @RestResource(path = "by-status", rel = "byStatus")
    List<Reservation> findByStatus(@Param("status") EReservationStatus status);

    @RestResource(path = "active-for-book", rel = "activeForBook")
    List<Reservation> findByBookIdAndStatusInOrderByReservationDateAsc(
            @Param("bookId") Long bookId, @Param("status") List<EReservationStatus> statuses);
}
