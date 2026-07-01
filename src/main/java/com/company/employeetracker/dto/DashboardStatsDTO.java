package com.company.employeetracker.dto;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {
    private Long totalEmployees;
    private Long totalDepartments;
    private Double averageScore;
    private String highestRatedEmployee;
    private String lowestRatedEmployee;
    private Long pendingGoalsCount;
    private Integer promotionEligibleCount;
    private Map<String, Long> departmentDistribution;
    private Map<String, Long> skillDistribution;
    private Map<String, Long> ratingDistribution;
    private Map<String, Double> monthlyTrend;
}
