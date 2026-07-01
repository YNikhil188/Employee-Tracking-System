package com.company.employeetracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_skills", 
       uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "skill_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "skill_level", nullable = false, length = 50)
    private String skillLevel; // BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}
