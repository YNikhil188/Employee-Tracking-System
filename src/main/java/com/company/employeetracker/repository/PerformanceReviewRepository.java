package com.company.employeetracker.repository;

import com.company.employeetracker.entity.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    List<PerformanceReview> findByEmployeeIdOrderByReviewDateDesc(Long employeeId);
    
    @Query("SELECT AVG(pr.overallScore) FROM PerformanceReview pr WHERE pr.employee.id = :employeeId")
    Double getAverageScoreByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT AVG(pr.overallScore) FROM PerformanceReview pr")
    Double getSystemAverageScore();

    @Query("SELECT pr.overallRating, COUNT(pr) FROM PerformanceReview pr GROUP BY pr.overallRating")
    List<Object[]> getRatingDistribution();

    @Query("SELECT FUNCTION('MONTHNAME', pr.reviewDate), AVG(pr.overallScore) FROM PerformanceReview pr GROUP BY FUNCTION('MONTHNAME', pr.reviewDate), FUNCTION('MONTH', pr.reviewDate) ORDER BY FUNCTION('MONTH', pr.reviewDate)")
    List<Object[]> getMonthlyTrend();
}
