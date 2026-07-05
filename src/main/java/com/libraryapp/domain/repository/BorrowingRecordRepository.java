package com.libraryapp.domain.repository;

import com.libraryapp.domain.entity.BorrowingRecord;
import com.libraryapp.domain.enums.EBorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(path = "borrowing-records", collectionResourceRel = "borrowingRecords")
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    @RestResource(path = "by-user", rel = "byUser")
    List<BorrowingRecord> findByUserId(@Param("userId") Long userId);

    @RestResource(path = "by-book", rel = "byBook")
    List<BorrowingRecord> findByBookId(@Param("bookId") Long bookId);

    @RestResource(path = "by-status", rel = "byStatus")
    List<BorrowingRecord> findByStatus(@Param("status") EBorrowingStatus status);

    // To retrieve list of overdue records
    @RestResource(path = "by-status-and-due-date", rel = "byStatusAndDueDate")
    List<BorrowingRecord> findByStatusAndDueDateBefore(@Param("status") EBorrowingStatus status, @Param("date") LocalDate date);

    @RestResource(path = "by-user-and-status", rel = "byUserAndStatus")
    List<BorrowingRecord> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") EBorrowingStatus status);

    @RestResource(path = "active-for-book", rel = "activeForBook")
    List<BorrowingRecord> findByBookIdAndStatusInOrderByDueDateAsc(
            @Param("bookId") Long bookId, @Param("status") List<EBorrowingStatus> statuses);
}
