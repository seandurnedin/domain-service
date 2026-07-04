package com.libraryapp.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 */
@Data
@Builder
public class OverdueRecordDto {

    private Long borrowingRecordId;
    private Long userId;
    private String userEmail;
    private Long bookId;
    private String bookTitle;
    private LocalDate dueDate;
    private Long daysOverdue;
    private BigDecimal calculatedLateFee;
}
