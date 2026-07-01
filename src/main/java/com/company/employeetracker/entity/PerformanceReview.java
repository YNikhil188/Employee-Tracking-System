package com.company.employeetracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "performance_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @Column(name = "technical_knowledge", nullable = false)
    private Integer technicalKnowledge; // Score: 0 to 100

    @Column(nullable = false)
    private Integer communication; // Score: 0 to 100

    @Column(nullable = false)
    private Integer teamwork; // Score: 0 to 100

    @Column(nullable = false)
    private Integer leadership; // Score: 0 to 100

    @Column(nullable = false)
    private Integer attendance; // Score: 0 to 100 (e.g. dynamic or manually graded)

    @Column(name = "problem_solving", nullable = false)
    private Integer problemSolving; // Score: 0 to 100

    @Column(nullable = false)
    private Integer innovation; // Score: 0 to 100

    @Column(nullable = false)
    private Integer discipline; // Score: 0 to 100

    @Column(length = 500)
    private String remarks;

    @Column(name = "review_date", nullable = false)
    private LocalDate reviewDate;

    @Column(name = "overall_score", nullable = false)
    private Double overallScore;

    @Column(name = "overall_rating", nullable = false, length = 50)
    private String overallRating; // Excellent, Very Good, Good, Average, Needs Improvement
}
