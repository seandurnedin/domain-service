package com.libraryapp.domain.service.impl;

import com.libraryapp.domain.dto.BookAvailabilityDto;
import com.libraryapp.domain.dto.OverdueRecordDto;
import com.libraryapp.domain.dto.UserBorrowingHistoryDto;
import com.libraryapp.domain.dto.UserProfileDto;
import com.libraryapp.domain.entity.Book;
import com.libraryapp.domain.entity.Reservation;
import com.libraryapp.domain.entity.User;
import com.libraryapp.domain.enums.EBorrowingStatus;
import com.libraryapp.domain.enums.EPaymentStatus;
import com.libraryapp.domain.enums.EReservationStatus;
import com.libraryapp.domain.exception.ResourceNotFoundException;
import com.libraryapp.domain.repository.*;
import com.libraryapp.domain.service.DomainQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DomainQueryServiceImpl implements DomainQueryService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    // Combines User Entity, Active Loans, Reservations and Payment outstanding
    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        long activeLoans = borrowingRecordRepository.findByUserIdAndStatus(userId, EBorrowingStatus.ON_LOAN).size()
                + borrowingRecordRepository.findByUserIdAndStatus(userId, EBorrowingStatus.OVERDUE).size();

        long activeReservations = reservationRepository.findByUserId(userId).stream()
                .filter(r -> r.getStatus() == EReservationStatus.RESERVED || r.getStatus() == EReservationStatus.NOTIFIED)
                .count();

        BigDecimal outstanding = paymentRepository.findByUserIdAndStatus(userId, EPaymentStatus.PENDING).stream()
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return UserProfileDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .status(user.getStatus().name())
                .role(user.getRole().name())
                .userGroupName(user.getUserGroup() != null ? user.getUserGroup().getName() : null)
                .maxBooksAllowed(user.getUserGroup() != null ? user.getUserGroup().getMaxBooksAllowed() : null)
                .loanDurationDays(user.getUserGroup() != null ? user.getUserGroup().getLoanDurationDays() : null)
                .activeLoanCount((int) activeLoans)
                .activeReservationCount((int) activeReservations)
                .outstandingBalance(outstanding)
                .build();
    }

    // Gets book by ID and List of reservations of books
    @Override
    @Transactional(readOnly = true)
    public BookAvailabilityDto getBookAvailability(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));

        List<Reservation> queue = reservationRepository.findByBookIdAndStatusOrderByReservationDateAsc(bookId, EReservationStatus.RESERVED);

        return BookAvailabilityDto.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .status(book.getStatus().name())
                .availableCopies(book.getAvailableCopies())
                .totalCopies(book.getTotalCopies())
                .queueLength(queue.size())
                .nextInQueueUserId(queue.isEmpty() ? null : queue.getFirst().getUser().getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBorrowingHistoryDto> getUserBorrowingHistory(Long userId) {
        return borrowingRecordRepository.findByUserId(userId).stream()
                .map(r -> UserBorrowingHistoryDto.builder()
                        .borrowingRecordId(r.getId())
                        .bookId(r.getBook().getId())
                        .bookTitle(r.getBook().getTitle())
                        .borrowDate(r.getBorrowDate())
                        .dueDate(r.getDueDate())
                        .returnDate(r.getReturnDate())
                        .status(r.getStatus().name())
                        .lateFee(r.getLateFee())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OverdueRecordDto> getOverdueRecords() {
        LocalDate today = LocalDate.now();
        return borrowingRecordRepository.findByStatusAndDueDateBefore(EBorrowingStatus.ON_LOAN, today).stream()
                .map(r -> {
                    long daysOverdue = ChronoUnit.DAYS.between(r.getDueDate(), today);
                    BigDecimal dailyFee = r.getUser().getUserGroup() != null
                            ? r.getUser().getUserGroup().getDailyLateFee()
                            : new BigDecimal("0.50");
                    return OverdueRecordDto.builder()
                            .borrowingRecordId(r.getId())
                            .userId(r.getUser().getId())
                            .userEmail(r.getUser().getEmail())
                            .bookId(r.getBook().getId())
                            .bookTitle(r.getBook().getTitle())
                            .dueDate(r.getDueDate())
                            .daysOverdue(daysOverdue)
                            .calculatedLateFee(dailyFee.multiply(BigDecimal.valueOf(daysOverdue)).setScale(2, RoundingMode.HALF_UP))
                            .build();
                })
                .toList();
    }
}
