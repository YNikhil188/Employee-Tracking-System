package com.company.employeetracker.controller;

import com.company.employeetracker.dto.GoalDTO;
import com.company.employeetracker.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<GoalDTO> assignGoal(@Valid @RequestBody GoalDTO goalDTO) {
        return new ResponseEntity<>(goalService.assignGoal(goalDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<GoalDTO> updateGoalProgress(
            @PathVariable Long id,
            @RequestParam(required = false) Integer progress,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(goalService.updateGoalProgress(id, progress, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<GoalDTO>> getEmployeeGoals(@PathVariable Long employeeId) {
        return ResponseEntity.ok(goalService.getEmployeeGoals(employeeId));
    }
}
