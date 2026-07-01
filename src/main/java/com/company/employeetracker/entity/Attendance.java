package com.company.employeetracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attendance", 
       uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "month_year"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "attendance_percentage", nullable = false)
    private Double attendancePercentage;

    @Column(name = "month_year", nullable = false, length = 10) // Format: "YYYY-MM"
    private String monthYear;
}
