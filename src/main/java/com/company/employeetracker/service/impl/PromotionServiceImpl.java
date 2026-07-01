package com.company.employeetracker.service.impl;

import com.company.employeetracker.dto.PromotionRecommendationDTO;
import com.company.employeetracker.entity.Employee;
import com.company.employeetracker.repository.AttendanceRepository;
import com.company.employeetracker.repository.EmployeeRepository;
import com.company.employeetracker.repository.GoalRepository;
import com.company.employeetracker.repository.PerformanceReviewRepository;
import com.company.employeetracker.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PromotionServiceImpl implements PromotionService {

    private final EmployeeRepository employeeRepository;
    private final PerformanceReviewRepository reviewRepository;
    private final AttendanceRepository attendanceRepository;
    private final GoalRepository goalRepository;

    public PromotionServiceImpl(EmployeeRepository employeeRepository,
                                PerformanceReviewRepository reviewRepository,
                                AttendanceRepository attendanceRepository,
                                GoalRepository goalRepository) {
        this.employeeRepository = employeeRepository;
        this.reviewRepository = reviewRepository;
        this.attendanceRepository = attendanceRepository;
        this.goalRepository = goalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionRecommendationDTO> getEligibleEmployees() {
        log.info("Running promotion recommendation engine");
        List<Employee> activeEmployees = employeeRepository.findAll().stream()
                .filter(e -> "ACTIVE".equalsIgnoreCase(e.getStatus()))
                .toList();

        List<PromotionRecommendationDTO> recommendations = new ArrayList<>();

        for (Employee emp : activeEmployees) {
            Double avgScore = reviewRepository.getAverageScoreByEmployeeId(emp.getId());
            if (avgScore == null) avgScore = 0.0;

            Double avgAttendance = attendanceRepository.getAverageAttendanceByEmployeeId(emp.getId());
            if (avgAttendance == null) avgAttendance = 0.0;

            long pendingGoals = goalRepository.countPendingGoalsByEmployeeId(emp.getId());

            // Calculate experience in years accurately
            double experienceYears = ChronoUnit.DAYS.between(emp.getJoiningDate(), LocalDate.now()) / 365.25;

            // Engine Rules check:
            // 1. Average Score > 85
            // 2. Attendance > 95%
            // 3. Minimum Experience 2 Years
            // 4. No Pending Goals
            if (avgScore > 85.0 && avgAttendance > 95.0 && experienceYears >= 2.0 && pendingGoals == 0) {
                recommendations.add(PromotionRecommendationDTO.builder()
                        .employeeId(emp.getId())
                        .employeeCode(emp.getEmployeeCode())
                        .employeeName(emp.getFirstName() + " " + emp.getLastName())
                        .departmentName(emp.getDepartment() != null ? emp.getDepartment().getName() : "N/A")
                        .designation(emp.getDesignation())
                        .joiningDate(emp.getJoiningDate())
                        .averageScore(avgScore)
                        .averageAttendance(avgAttendance)
                        .experienceYears(experienceYears)
                        .pendingGoalsCount(pendingGoals)
                        .build());
            }
        }

        log.info("Promotion engine completed. Found {} eligible employees.", recommendations.size());
        return recommendations;
    }
}
