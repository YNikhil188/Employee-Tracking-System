package com.company.employeetracker.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDTO {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    @NotNull(message = "Attendance percentage is required")
    @DecimalMin(value = "0.0", message = "Percentage cannot be less than 0")
    @DecimalMax(value = "100.0", message = "Percentage cannot be more than 100")
    private Double attendancePercentage;

    @NotBlank(message = "Month and Year is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "Month and Year must be in YYYY-MM format")
    private String monthYear; // e.g. "2026-07"
}
