package com.libraryapp.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class UserBorrowingHistoryDto {
    
    private Long borrowingRecordId;
    private Long bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private BigDecimal lateFee;
}
