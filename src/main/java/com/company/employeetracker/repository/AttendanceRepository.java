package com.company.employeetracker.repository;

import com.company.employeetracker.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeId(Long employeeId);
    Optional<Attendance> findByEmployeeIdAndMonthYear(Long employeeId, String monthYear);
    
    @Query("SELECT AVG(a.attendancePercentage) FROM Attendance a WHERE a.employee.id = :employeeId")
    Double getAverageAttendanceByEmployeeId(@Param("employeeId") Long employeeId);
}
