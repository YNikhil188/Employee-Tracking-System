package com.company.employeetracker.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalDTO {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    @NotBlank(message = "Goal name is required")
    @Size(max = 150, message = "Goal name must not exceed 150 characters")
    private String goalName;

    @NotNull(message = "Deadline is required")
    @FutureOrPresent(message = "Deadline must be today or in the future")
    private LocalDate deadline;

    @Min(value = 0, message = "Progress cannot be less than 0")
    @Max(value = 100, message = "Progress cannot be more than 100")
    private Integer progress;

    @NotBlank(message = "Status is required")
    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED
}
