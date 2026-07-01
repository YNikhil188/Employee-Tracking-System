package com.company.employeetracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "goal_name", nullable = false, length = 150)
    private String goalName;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(nullable = false)
    @Builder.Default
    private Integer progress = 0; // Range: 0 to 100

    @Column(nullable = false, length = 50)
    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED
}
