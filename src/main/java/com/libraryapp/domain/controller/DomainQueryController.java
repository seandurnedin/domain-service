package com.libraryapp.domain.controller;

import com.libraryapp.domain.dto.BookAvailabilityDto;
import com.libraryapp.domain.dto.OverdueRecordDto;
import com.libraryapp.domain.dto.UserBorrowingHistoryDto;
import com.libraryapp.domain.dto.UserProfileDto;
import com.libraryapp.domain.service.DomainQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/query")
@RequiredArgsConstructor
public class DomainQueryController {

    private final DomainQueryService domainQueryService;

    @GetMapping("/users/{userId}/profile")
    public UserProfileDto getUserProfile(@PathVariable Long userId) {
        return domainQueryService.getUserProfile(userId);
    }

    @GetMapping("/books/{bookId}/availability")
    public BookAvailabilityDto getBookAvailability(@PathVariable Long bookId) {
        return domainQueryService.getBookAvailability(bookId);
    }

    @GetMapping("/users/{userId}/borrowing-history")
    public List<UserBorrowingHistoryDto> getUserBorrowingHistory(@PathVariable Long userId) {
        return domainQueryService.getUserBorrowingHistory(userId);
    }

    @GetMapping("/borrowing-records/overdue")
    public List<OverdueRecordDto> getOverdueRecords() {
        return domainQueryService.getOverdueRecords();
    }
}
