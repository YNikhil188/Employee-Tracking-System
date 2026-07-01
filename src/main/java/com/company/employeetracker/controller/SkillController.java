package com.company.employeetracker.controller;

import com.company.employeetracker.dto.EmployeeSkillDTO;
import com.company.employeetracker.dto.SkillDTO;
import com.company.employeetracker.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    // Master Skill endpoints
    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<SkillDTO> createSkill(@Valid @RequestBody SkillDTO skillDTO) {
        return new ResponseEntity<>(skillService.createSkill(skillDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<SkillDTO> updateSkill(@PathVariable Long id, @Valid @RequestBody SkillDTO skillDTO) {
        return ResponseEntity.ok(skillService.updateSkill(id, skillDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }

    // Employee Skill endpoints
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeSkillDTO>> getEmployeeSkills(@PathVariable Long employeeId) {
        return ResponseEntity.ok(skillService.getEmployeeSkills(employeeId));
    }

    @PostMapping("/employee")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<EmployeeSkillDTO> assignSkillToEmployee(@Valid @RequestBody EmployeeSkillDTO employeeSkillDTO) {
        return new ResponseEntity<>(skillService.assignSkillToEmployee(employeeSkillDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/employee/{employeeSkillId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<Void> removeSkillFromEmployee(@PathVariable Long employeeSkillId) {
        skillService.removeSkillFromEmployee(employeeSkillId);
        return ResponseEntity.noContent().build();
    }
}
