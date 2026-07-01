package com.company.employeetracker.repository;

import com.company.employeetracker.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByEmployeeId(Long employeeId);
    
    // Count pending/in progress goals for promotion calculations
    @Query("SELECT COUNT(g) FROM Goal g WHERE g.employee.id = :employeeId AND (g.status = 'PENDING' OR g.status = 'IN_PROGRESS')")
    long countPendingGoalsByEmployeeId(@Param("employeeId") Long employeeId);
}
