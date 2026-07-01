package com.company.employeetracker.service.impl;

import com.company.employeetracker.dto.EmployeeDTO;
import com.company.employeetracker.entity.Department;
import com.company.employeetracker.entity.Employee;
import com.company.employeetracker.exception.DepartmentNotFoundException;
import com.company.employeetracker.exception.DuplicateEmployeeException;
import com.company.employeetracker.exception.EmployeeNotFoundException;
import com.company.employeetracker.repository.DepartmentRepository;
import com.company.employeetracker.repository.EmployeeRepository;
import com.company.employeetracker.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, 
                               DepartmentRepository departmentRepository, 
                               ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAllEmployees(String term, Long departmentId, String status, String designation,
                                              int page, int size, String sortBy, String sortDir) {
        log.info("Fetching employees page: {}, size: {}, sort: {} {}", page, size, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employeePage = employeeRepository.findWithFilters(
                term, departmentId, status, designation, pageable);

        return employeePage.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return convertToDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByCode(String code) {
        log.info("Fetching employee with code: {}", code);
        Employee employee = employeeRepository.findByEmployeeCode(code)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with code: " + code));
        return convertToDto(employee);
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        log.info("Creating new employee with code: {}", employeeDTO.getEmployeeCode());

        if (employeeRepository.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
            throw new DuplicateEmployeeException("Employee code '" + employeeDTO.getEmployeeCode() + "' is already in use");
        }

        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new DuplicateEmployeeException("Email '" + employeeDTO.getEmail() + "' is already registered");
        }

        Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + employeeDTO.getDepartmentId()));

        Employee employee = convertToEntity(employeeDTO);
        employee.setDepartment(department);

        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDto(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        log.info("Updating employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        if (employeeRepository.existsByEmployeeCodeAndIdNot(employeeDTO.getEmployeeCode(), id)) {
            throw new DuplicateEmployeeException("Employee code '" + employeeDTO.getEmployeeCode() + "' is already in use");
        }

        if (employeeRepository.existsByEmailAndIdNot(employeeDTO.getEmail(), id)) {
            throw new DuplicateEmployeeException("Email '" + employeeDTO.getEmail() + "' is already registered");
        }

        Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + employeeDTO.getDepartmentId()));

        employee.setEmployeeCode(employeeDTO.getEmployeeCode());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setGender(employeeDTO.getGender());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setJoiningDate(employeeDTO.getJoiningDate());
        employee.setDesignation(employeeDTO.getDesignation());
        employee.setSalary(employeeDTO.getSalary());
        employee.setStatus(employeeDTO.getStatus());
        employee.setDepartment(department);

        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDto(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUniqueDesignations() {
        return employeeRepository.findUniqueDesignations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> globalSearch(String query) {
        log.info("Executing global search for query: {}", query);
        if (query == null || query.trim().isEmpty()) {
            return employeeRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
        return employeeRepository.globalSearch(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private EmployeeDTO convertToDto(Employee employee) {
        EmployeeDTO dto = modelMapper.map(employee, EmployeeDTO.class);
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        return dto;
    }

    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = modelMapper.map(dto, Employee.class);
        // Department is set separately in service calls
        return employee;
    }
}
