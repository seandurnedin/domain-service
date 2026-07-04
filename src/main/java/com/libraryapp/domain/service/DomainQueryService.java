package com.libraryapp.domain.service;

import com.libraryapp.domain.dto.BookAvailabilityDto;
import com.libraryapp.domain.dto.OverdueRecordDto;
import com.libraryapp.domain.dto.UserBorrowingHistoryDto;
import com.libraryapp.domain.dto.UserProfileDto;

import java.util.List;

public interface DomainQueryService {

    UserProfileDto getUserProfile(Long userId);

    BookAvailabilityDto getBookAvailability(Long bookId);

    List<UserBorrowingHistoryDto> getUserBorrowingHistory(Long userId);

    List<OverdueRecordDto> getOverdueRecords();
}
