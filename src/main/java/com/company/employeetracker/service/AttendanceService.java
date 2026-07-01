package com.company.employeetracker.service;

import com.company.employeetracker.dto.AttendanceDTO;
import java.util.List;

public interface AttendanceService {
    AttendanceDTO logAttendance(AttendanceDTO attendanceDTO);
    List<AttendanceDTO> getEmployeeAttendance(Long employeeId);
    Double getAverageAttendance(Long employeeId);
}
