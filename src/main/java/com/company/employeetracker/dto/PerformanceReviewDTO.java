package com.company.employeetracker.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReviewDTO {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    private Long managerId;

    private String managerName;

    @NotNull(message = "Technical knowledge score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer technicalKnowledge;

    @NotNull(message = "Communication score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer communication;

    @NotNull(message = "Teamwork score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer teamwork;

    @NotNull(message = "Leadership score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer leadership;

    @NotNull(message = "Attendance score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer attendance;

    @NotNull(message = "Problem solving score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer problemSolving;

    @NotNull(message = "Innovation score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer innovation;

    @NotNull(message = "Discipline score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer discipline;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    private LocalDate reviewDate;

    private Double overallScore;

    private String overallRating;
}
