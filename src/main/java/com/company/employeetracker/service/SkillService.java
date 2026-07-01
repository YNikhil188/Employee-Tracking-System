package com.company.employeetracker.service;

import com.company.employeetracker.dto.EmployeeSkillDTO;
import com.company.employeetracker.dto.SkillDTO;
import java.util.List;

public interface SkillService {
    // Master Skill CRUD
    List<SkillDTO> getAllSkills();
    SkillDTO getSkillById(Long id);
    SkillDTO createSkill(SkillDTO skillDTO);
    SkillDTO updateSkill(Long id, SkillDTO skillDTO);
    void deleteSkill(Long id);

    // Employee Skill Assignment
    List<EmployeeSkillDTO> getEmployeeSkills(Long employeeId);
    EmployeeSkillDTO assignSkillToEmployee(EmployeeSkillDTO employeeSkillDTO);
    void removeSkillFromEmployee(Long employeeSkillId);
}
