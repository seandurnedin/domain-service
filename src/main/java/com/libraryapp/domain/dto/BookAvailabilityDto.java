package com.libraryapp.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Joins Entities: Book, BorrowingRecord, Reservation
 */
@Data
@Builder
public class BookAvailabilityDto {

    private Long bookId;
    private String title;
    private String isbn;
    private String status;
    private Integer availableCopies;
    private Integer totalCopies;
    private Integer queueLength;
    private Long nextInQueueUserId;
}
