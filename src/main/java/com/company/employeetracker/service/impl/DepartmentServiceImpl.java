package com.company.employeetracker.service.impl;

import com.company.employeetracker.dto.DepartmentDTO;
import com.company.employeetracker.entity.Department;
import com.company.employeetracker.exception.DepartmentNotFoundException;
import com.company.employeetracker.repository.DepartmentRepository;
import com.company.employeetracker.repository.EmployeeRepository;
import com.company.employeetracker.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, 
                                 EmployeeRepository employeeRepository, 
                                 ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        log.info("Fetching all departments");
        return departmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Long id) {
        log.info("Fetching department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        return convertToDto(department);
    }

    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        log.info("Creating new department with name: {}", departmentDTO.getName());
        if (departmentRepository.existsByName(departmentDTO.getName())) {
            throw new IllegalArgumentException("Department with name '" + departmentDTO.getName() + "' already exists");
        }
        Department department = convertToEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return convertToDto(savedDepartment);
    }

    @Override
    @Transactional
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        log.info("Updating department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

        // Check name uniqueness if changed
        if (!department.getName().equalsIgnoreCase(departmentDTO.getName()) 
                && departmentRepository.existsByName(departmentDTO.getName())) {
            throw new IllegalArgumentException("Department with name '" + departmentDTO.getName() + "' already exists");
        }

        department.setName(departmentDTO.getName());
        department.setManager(departmentDTO.getManager());
        department.setLocation(departmentDTO.getLocation());
        department.setDescription(departmentDTO.getDescription());

        Department updatedDepartment = departmentRepository.save(department);
        return convertToDto(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        
        long employeeCount = employeeRepository.countByDepartmentId(id);
        if (employeeCount > 0) {
            throw new IllegalStateException("Cannot delete department because it has active employees assigned to it");
        }

        departmentRepository.delete(department);
    }

    private DepartmentDTO convertToDto(Department department) {
        DepartmentDTO dto = modelMapper.map(department, DepartmentDTO.class);
        dto.setEmployeeCount((int) employeeRepository.countByDepartmentId(department.getId()));
        return dto;
    }

    private Department convertToEntity(DepartmentDTO dto) {
        return modelMapper.map(dto, Department.class);
    }
}
