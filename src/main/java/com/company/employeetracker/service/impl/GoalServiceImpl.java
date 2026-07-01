package com.company.employeetracker.service.impl;

import com.company.employeetracker.dto.GoalDTO;
import com.company.employeetracker.entity.Employee;
import com.company.employeetracker.entity.Goal;
import com.company.employeetracker.exception.EmployeeNotFoundException;
import com.company.employeetracker.repository.EmployeeRepository;
import com.company.employeetracker.repository.GoalRepository;
import com.company.employeetracker.service.GoalService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public GoalServiceImpl(GoalRepository goalRepository,
                           EmployeeRepository employeeRepository,
                           ModelMapper modelMapper) {
        this.goalRepository = goalRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public GoalDTO assignGoal(GoalDTO dto) {
        log.info("Assigning new goal '{}' to employee id: {}", dto.getGoalName(), dto.getEmployeeId());
        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        Goal goal = Goal.builder()
                .employee(employee)
                .goalName(dto.getGoalName())
                .deadline(dto.getDeadline())
                .progress(dto.getProgress() != null ? dto.getProgress() : 0)
                .status(dto.getStatus() != null ? dto.getStatus().toUpperCase() : "PENDING")
                .build();

        // Business Rule: Auto-align status if progress starts at 100
        if (goal.getProgress() == 100) {
            goal.setStatus("COMPLETED");
        }

        Goal saved = goalRepository.save(goal);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public GoalDTO updateGoalProgress(Long id, Integer progress, String status) {
        log.info("Updating goal progress/status for goal id: {}", id);
        
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found with id: " + id));

        if (status != null) {
            goal.setStatus(status.toUpperCase());
        }

        if (progress != null) {
            goal.setProgress(progress);
            // Business rule alignment
            if (progress == 100) {
                goal.setStatus("COMPLETED");
            } else if (progress > 0 && "PENDING".equalsIgnoreCase(goal.getStatus())) {
                goal.setStatus("IN_PROGRESS");
            }
        }

        if ("COMPLETED".equalsIgnoreCase(goal.getStatus())) {
            goal.setProgress(100);
        }

        Goal updated = goalRepository.save(goal);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    public void deleteGoal(Long id) {
        log.info("Deleting goal with id: {}", id);
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found with id: " + id));
        goalRepository.delete(goal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalDTO> getEmployeeGoals(Long employeeId) {
        log.info("Fetching goals for employee id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        return goalRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private GoalDTO convertToDto(Goal goal) {
        GoalDTO dto = modelMapper.map(goal, GoalDTO.class);
        if (goal.getEmployee() != null) {
            dto.setEmployeeId(goal.getEmployee().getId());
            dto.setEmployeeName(goal.getEmployee().getFirstName() + " " + goal.getEmployee().getLastName());
        }
        return dto;
    }
}
