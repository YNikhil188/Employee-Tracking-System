package com.company.employeetracker.controller;

import com.company.employeetracker.dto.AttendanceDTO;
import com.company.employeetracker.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<AttendanceDTO> logAttendance(@Valid @RequestBody AttendanceDTO attendanceDTO) {
        return new ResponseEntity<>(attendanceService.logAttendance(attendanceDTO), HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceDTO>> getEmployeeAttendance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getEmployeeAttendance(employeeId));
    }

    @GetMapping("/employee/{employeeId}/average")
    public ResponseEntity<Double> getAverageAttendance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getAverageAttendance(employeeId));
    }
}
