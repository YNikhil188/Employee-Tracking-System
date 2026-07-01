package com.company.employeetracker.controller;

import com.company.employeetracker.dto.PerformanceReviewDTO;
import com.company.employeetracker.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PerformanceReviewDTO> submitReview(
            @Valid @RequestBody PerformanceReviewDTO reviewDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String managerUsername = userDetails.getUsername();
        return new ResponseEntity<>(reviewService.submitReview(reviewDTO, managerUsername), HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PerformanceReviewDTO>> getEmployeeReviews(@PathVariable Long employeeId) {
        return ResponseEntity.ok(reviewService.getEmployeeReviews(employeeId));
    }

    @GetMapping("/employee/{employeeId}/average")
    public ResponseEntity<Double> getAverageScore(@PathVariable Long employeeId) {
        return ResponseEntity.ok(reviewService.getAverageScore(employeeId));
    }
}
