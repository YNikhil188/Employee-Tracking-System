package com.company.employeetracker.service.impl;

import com.company.employeetracker.dto.DashboardStatsDTO;
import com.company.employeetracker.entity.Employee;
import com.company.employeetracker.entity.EmployeeSkill;
import com.company.employeetracker.entity.Goal;
import com.company.employeetracker.entity.PerformanceReview;
import com.company.employeetracker.repository.*;
import com.company.employeetracker.service.DashboardService;
import com.company.employeetracker.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PerformanceReviewRepository reviewRepository;
    private final GoalRepository goalRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final PromotionService promotionService;

    public DashboardServiceImpl(EmployeeRepository employeeRepository,
                                DepartmentRepository departmentRepository,
                                PerformanceReviewRepository reviewRepository,
                                GoalRepository goalRepository,
                                EmployeeSkillRepository employeeSkillRepository,
                                PromotionService promotionService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.reviewRepository = reviewRepository;
        this.goalRepository = goalRepository;
        this.employeeSkillRepository = employeeSkillRepository;
        this.promotionService = promotionService;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        log.info("Aggregating dashboard statistics");

        long totalEmployees = employeeRepository.count();
        long totalDepartments = departmentRepository.count();

        // Calculate Average Performance Score
        List<PerformanceReview> allReviews = reviewRepository.findAll();
        double averageScore = allReviews.stream()
                .mapToDouble(PerformanceReview::getOverallScore)
                .average()
                .orElse(0.0);

        // Highest and Lowest Rated Employees
        String highestRated = "None";
        String lowestRated = "None";
        if (!allReviews.isEmpty()) {
            PerformanceReview maxReview = allReviews.stream()
                    .max(Comparator.comparing(PerformanceReview::getOverallScore))
                    .orElse(null);
            if (maxReview != null && maxReview.getEmployee() != null) {
                highestRated = maxReview.getEmployee().getFirstName() + " " + maxReview.getEmployee().getLastName() 
                        + " (" + String.format("%.1f", maxReview.getOverallScore()) + ")";
            }

            PerformanceReview minReview = allReviews.stream()
                    .min(Comparator.comparing(PerformanceReview::getOverallScore))
                    .orElse(null);
            if (minReview != null && minReview.getEmployee() != null) {
                lowestRated = minReview.getEmployee().getFirstName() + " " + minReview.getEmployee().getLastName() 
                        + " (" + String.format("%.1f", minReview.getOverallScore()) + ")";
            }
        }

        // Count pending goals
        long pendingGoalsCount = goalRepository.findAll().stream()
                .filter(g -> "PENDING".equalsIgnoreCase(g.getStatus()) || "IN_PROGRESS".equalsIgnoreCase(g.getStatus()))
                .count();

        // Promotion Recommendations count
        int promotionEligibleCount = promotionService.getEligibleEmployees().size();

        // Department Distribution map
        Map<String, Long> departmentDistribution = employeeRepository.findAll().stream()
                .filter(e -> e.getDepartment() != null)
                .collect(Collectors.groupingBy(e -> e.getDepartment().getName(), Collectors.counting()));

        // Skill Distribution map (top 5 skills)
        Map<String, Long> skillDistribution = employeeSkillRepository.findAll().stream()
                .filter(es -> es.getSkill() != null)
                .collect(Collectors.groupingBy(es -> es.getSkill().getName(), Collectors.counting()));

        // Rating Distribution map (Excellent, Very Good, etc.)
        Map<String, Long> ratingDistribution = allReviews.stream()
                .collect(Collectors.groupingBy(PerformanceReview::getOverallRating, Collectors.counting()));

        // Monthly trend mapping (Monthly Averages)
        // Group by Month (Name representation)
        Map<String, Double> monthlyTrend = allReviews.stream()
                .filter(r -> r.getReviewDate() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getReviewDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        TreeMap::new, // Keep sorted order
                        Collectors.averagingDouble(PerformanceReview::getOverallScore)
                ));

        return DashboardStatsDTO.builder()
                .totalEmployees(totalEmployees)
                .totalDepartments(totalDepartments)
                .averageScore(averageScore)
                .highestRatedEmployee(highestRated)
                .lowestRatedEmployee(lowestRated)
                .pendingGoalsCount(pendingGoalsCount)
                .promotionEligibleCount(promotionEligibleCount)
                .departmentDistribution(departmentDistribution)
                .skillDistribution(skillDistribution)
                .ratingDistribution(ratingDistribution)
                .monthlyTrend(monthlyTrend)
                .build();
    }
}
