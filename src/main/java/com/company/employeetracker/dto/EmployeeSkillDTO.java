package com.company.employeetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSkillDTO {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    private String skillName;

    @NotBlank(message = "Skill level is required")
    private String skillLevel; // BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}
