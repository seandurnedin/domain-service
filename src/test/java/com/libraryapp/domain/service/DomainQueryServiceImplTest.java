package com.libraryapp.domain.service;

import com.libraryapp.domain.dto.BookAvailabilityDto;
import com.libraryapp.domain.dto.OverdueRecordDto;
import com.libraryapp.domain.dto.UserBorrowingHistoryDto;
import com.libraryapp.domain.dto.UserProfileDto;
import com.libraryapp.domain.entity.*;
import com.libraryapp.domain.enums.EBorrowingStatus;
import com.libraryapp.domain.enums.EPaymentStatus;
import com.libraryapp.domain.enums.EPaymentType;
import com.libraryapp.domain.enums.EReservationStatus;
import com.libraryapp.domain.exception.ResourceNotFoundException;
import com.libraryapp.domain.repository.*;
import com.libraryapp.domain.service.impl.DomainQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DomainQueryServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private DomainQueryServiceImpl domainQueryService;

    private User user;
    private UserGroup userGroup;
    private Book book;

    @BeforeEach
    void setUp() {
        userGroup = UserGroup.builder()
                .id(1L)
                .name("USER")
                .maxBooksAllowed(5)
                .loanDurationDays(14)
                .dailyLateFee(new BigDecimal("0.50"))
                .build();

        user = User.builder()
                .id(10L)
                .username("sam1234")
                .email("sam@gmail.com")
                .fullName("Sam Tan")
                .userGroup(userGroup)
                .build();

        book = Book.builder()
                .id(100L)
                .isbn("9780134685991")
                .title("Under The Dome")
                .author("Stephen King")
                .totalCopies(3)
                .availableCopies(1)
                .build();
    }

    @Test
    void getUserProfile_aggregatesAcrossFourTables() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(borrowingRecordRepository.findByUserIdAndStatus(10L, EBorrowingStatus.ON_LOAN))
                .thenReturn(List.of(BorrowingRecord.builder().id(1L).build()));
        when(borrowingRecordRepository.findByUserIdAndStatus(10L, EBorrowingStatus.OVERDUE))
                .thenReturn(List.of());
        when(reservationRepository.findByUserId(10L)).thenReturn(List.of(
                Reservation.builder().id(1L).status(EReservationStatus.RESERVED).build()
        ));
        when(paymentRepository.findByUserIdAndStatus(10L, EPaymentStatus.PENDING)).thenReturn(List.of(
                Payment.builder().id(1L).amount(new BigDecimal("2.50")).type(EPaymentType.LATE_FEE).build()
        ));

        UserProfileDto profile = domainQueryService.getUserProfile(10L);

        assertThat(profile.getUsername()).isEqualTo("sam1234");
        assertThat(profile.getActiveLoanCount()).isEqualTo(1);
        assertThat(profile.getActiveReservationCount()).isEqualTo(1);
        assertThat(profile.getOutstandingBalance()).isEqualByComparingTo(new BigDecimal("2.50"));
        assertThat(profile.getUserGroupName()).isEqualTo("USER");
    }

    @Test
    void getUserProfile_userNotFound_emitsError() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> domainQueryService.getUserProfile(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getBookAvailability_returnsQueueLength() {
        when(bookRepository.findById(100L)).thenReturn(Optional.of(book));
        when(reservationRepository.findByBookIdAndStatusOrderByReservationDateAsc(100L, EReservationStatus.RESERVED))
                .thenReturn(List.of(Reservation.builder().id(2L).user(user).status(EReservationStatus.RESERVED).build()));

        BookAvailabilityDto dto = domainQueryService.getBookAvailability(100L);

        assertThat(dto.getTitle()).isEqualTo("Under The Dome");
        assertThat(dto.getQueueLength()).isEqualTo(1);
        assertThat(dto.getNextInQueueUserId()).isEqualTo(10L);
    }

    @Test
    void getBookAvailability_bookNotFound_emitsError() {
        when(bookRepository.findById(eq(404L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> domainQueryService.getBookAvailability(404L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getUserBorrowingHistory_mapsAllRecords() {
        BorrowingRecord record = BorrowingRecord.builder()
                .id(7L).user(user).book(book)
                .borrowDate(LocalDate.now().minusDays(10))
                .dueDate(LocalDate.now().minusDays(1))
                .status(EBorrowingStatus.OVERDUE)
                .lateFee(new BigDecimal("1.50"))
                .build();
        when(borrowingRecordRepository.findByUserId(10L)).thenReturn(List.of(record));

        List<UserBorrowingHistoryDto> history = domainQueryService.getUserBorrowingHistory(10L);

        assertThat(history).hasSize(1);
        UserBorrowingHistoryDto dto = history.get(0);
        assertThat(dto.getBookTitle()).isEqualTo("Under The Dome");
        assertThat(dto.getStatus()).isEqualTo("OVERDUE");
        assertThat(dto.getLateFee()).isEqualByComparingTo(new BigDecimal("1.50"));
    }

    @Test
    void getOverdueRecords_calculatesLateFeeFromUserGroupDailyRate() {
        BorrowingRecord overdue = BorrowingRecord.builder()
                .id(8L).user(user).book(book)
                .dueDate(LocalDate.now().minusDays(4))
                .status(EBorrowingStatus.ON_LOAN)
                .build();
        when(borrowingRecordRepository.findByStatusAndDueDateBefore(eq(EBorrowingStatus.ON_LOAN), any(LocalDate.class)))
                .thenReturn(List.of(overdue));

        List<OverdueRecordDto> records = domainQueryService.getOverdueRecords();

        assertThat(records).hasSize(1);
        OverdueRecordDto dto = records.get(0);
        assertThat(dto.getDaysOverdue()).isEqualTo(4);
        assertThat(dto.getCalculatedLateFee()).isEqualByComparingTo("2.00"); // 4 days * 0.5 daily fee
    }
}
