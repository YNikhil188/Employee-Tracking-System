package com.company.employeetracker.service;

import com.company.employeetracker.dto.GoalDTO;
import java.util.List;

public interface GoalService {
    GoalDTO assignGoal(GoalDTO goalDTO);
    GoalDTO updateGoalProgress(Long id, Integer progress, String status);
    void deleteGoal(Long id);
    List<GoalDTO> getEmployeeGoals(Long employeeId);
}
