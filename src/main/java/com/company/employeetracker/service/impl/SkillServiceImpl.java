package com.company.employeetracker.service.impl;

import com.company.employeetracker.dto.EmployeeSkillDTO;
import com.company.employeetracker.dto.SkillDTO;
import com.company.employeetracker.entity.Employee;
import com.company.employeetracker.entity.EmployeeSkill;
import com.company.employeetracker.entity.Skill;
import com.company.employeetracker.exception.EmployeeNotFoundException;
import com.company.employeetracker.repository.EmployeeRepository;
import com.company.employeetracker.repository.EmployeeSkillRepository;
import com.company.employeetracker.repository.SkillRepository;
import com.company.employeetracker.service.SkillService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public SkillServiceImpl(SkillRepository skillRepository,
                            EmployeeSkillRepository employeeSkillRepository,
                            EmployeeRepository employeeRepository,
                            ModelMapper modelMapper) {
        this.skillRepository = skillRepository;
        this.employeeSkillRepository = employeeSkillRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDTO> getAllSkills() {
        log.info("Fetching all master skills");
        return skillRepository.findAll().stream()
                .map(s -> modelMapper.map(s, SkillDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SkillDTO getSkillById(Long id) {
        log.info("Fetching master skill by id: {}", id);
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + id));
        return modelMapper.map(skill, SkillDTO.class);
    }

    @Override
    @Transactional
    public SkillDTO createSkill(SkillDTO skillDTO) {
        log.info("Creating master skill with name: {}", skillDTO.getName());
        if (skillRepository.existsByName(skillDTO.getName())) {
            throw new IllegalArgumentException("Skill with name '" + skillDTO.getName() + "' already exists");
        }
        Skill skill = modelMapper.map(skillDTO, Skill.class);
        Skill savedSkill = skillRepository.save(skill);
        return modelMapper.map(savedSkill, SkillDTO.class);
    }

    @Override
    @Transactional
    public SkillDTO updateSkill(Long id, SkillDTO skillDTO) {
        log.info("Updating master skill with id: {}", id);
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + id));

        if (!skill.getName().equalsIgnoreCase(skillDTO.getName()) && skillRepository.existsByName(skillDTO.getName())) {
            throw new IllegalArgumentException("Skill with name '" + skillDTO.getName() + "' already exists");
        }

        skill.setName(skillDTO.getName());
        skill.setDescription(skillDTO.getDescription());
        
        Skill updatedSkill = skillRepository.save(skill);
        return modelMapper.map(updatedSkill, SkillDTO.class);
    }

    @Override
    @Transactional
    public void deleteSkill(Long id) {
        log.info("Deleting master skill with id: {}", id);
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + id));
        skillRepository.delete(skill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeSkillDTO> getEmployeeSkills(Long employeeId) {
        log.info("Fetching skills for employee with id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        return employeeSkillRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeSkillDTO assignSkillToEmployee(EmployeeSkillDTO employeeSkillDTO) {
        log.info("Assigning skill {} to employee {}", employeeSkillDTO.getSkillId(), employeeSkillDTO.getEmployeeId());
        
        Employee employee = employeeRepository.findById(employeeSkillDTO.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeSkillDTO.getEmployeeId()));
        
        Skill skill = skillRepository.findById(employeeSkillDTO.getSkillId())
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + employeeSkillDTO.getSkillId()));

        if (employeeSkillRepository.findByEmployeeIdAndSkillId(employee.getId(), skill.getId()).isPresent()) {
            throw new IllegalArgumentException("Skill '" + skill.getName() + "' is already assigned to this employee");
        }

        EmployeeSkill employeeSkill = EmployeeSkill.builder()
                .employee(employee)
                .skill(skill)
                .skillLevel(employeeSkillDTO.getSkillLevel())
                .build();

        EmployeeSkill saved = employeeSkillRepository.save(employeeSkill);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public void removeSkillFromEmployee(Long employeeSkillId) {
        log.info("Removing employee skill association with id: {}", employeeSkillId);
        EmployeeSkill es = employeeSkillRepository.findById(employeeSkillId)
                .orElseThrow(() -> new IllegalArgumentException("Skill assignment not found with id: " + employeeSkillId));
        employeeSkillRepository.delete(es);
    }

    private EmployeeSkillDTO convertToDto(EmployeeSkill es) {
        return EmployeeSkillDTO.builder()
                .id(es.getId())
                .employeeId(es.getEmployee().getId())
                .skillId(es.getSkill().getId())
                .skillName(es.getSkill().getName())
                .skillLevel(es.getSkillLevel())
                .build();
    }
}
