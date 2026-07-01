package com.company.employeetracker.service.impl;

import com.company.employeetracker.dto.AttendanceDTO;
import com.company.employeetracker.entity.Attendance;
import com.company.employeetracker.entity.Employee;
import com.company.employeetracker.exception.EmployeeNotFoundException;
import com.company.employeetracker.repository.AttendanceRepository;
import com.company.employeetracker.repository.EmployeeRepository;
import com.company.employeetracker.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 EmployeeRepository employeeRepository,
                                 ModelMapper modelMapper) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public AttendanceDTO logAttendance(AttendanceDTO dto) {
        log.info("Logging attendance percentage {} for employee {} in month {}", 
                dto.getAttendancePercentage(), dto.getEmployeeId(), dto.getMonthYear());
        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        Optional<Attendance> existingOpt = attendanceRepository.findByEmployeeIdAndMonthYear(
                employee.getId(), dto.getMonthYear());

        Attendance attendance;
        if (existingOpt.isPresent()) {
            log.info("Overwriting existing attendance entry");
            attendance = existingOpt.get();
            attendance.setAttendancePercentage(dto.getAttendancePercentage());
        } else {
            attendance = Attendance.builder()
                    .employee(employee)
                    .attendancePercentage(dto.getAttendancePercentage())
                    .monthYear(dto.getMonthYear())
                    .build();
        }

        Attendance saved = attendanceRepository.save(attendance);
        return convertToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDTO> getEmployeeAttendance(Long employeeId) {
        log.info("Fetching attendance logs for employee id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        return attendanceRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageAttendance(Long employeeId) {
        log.info("Calculating average attendance for employee id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        Double avg = attendanceRepository.getAverageAttendanceByEmployeeId(employeeId);
        return avg != null ? avg : 0.0;
    }

    private AttendanceDTO convertToDto(Attendance attendance) {
        AttendanceDTO dto = modelMapper.map(attendance, AttendanceDTO.class);
        if (attendance.getEmployee() != null) {
            dto.setEmployeeId(attendance.getEmployee().getId());
            dto.setEmployeeName(attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName());
        }
        return dto;
    }
}
