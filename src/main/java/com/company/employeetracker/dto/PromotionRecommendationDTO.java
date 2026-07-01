package com.company.employeetracker.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionRecommendationDTO {
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private String departmentName;
    private String designation;
    private LocalDate joiningDate;
    private Double averageScore;
    private Double averageAttendance;
    private Double experienceYears;
    private Long pendingGoalsCount;
}
