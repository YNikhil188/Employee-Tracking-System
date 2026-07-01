package com.company.employeetracker.service;

import com.company.employeetracker.dto.PerformanceReviewDTO;
import java.util.List;

public interface ReviewService {
    PerformanceReviewDTO submitReview(PerformanceReviewDTO reviewDTO, String managerUsername);
    List<PerformanceReviewDTO> getEmployeeReviews(Long employeeId);
    Double getAverageScore(Long employeeId);
}
