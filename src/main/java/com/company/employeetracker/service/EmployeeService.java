package com.company.employeetracker.service;

import com.company.employeetracker.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import java.util.List;

public interface EmployeeService {
    Page<EmployeeDTO> getAllEmployees(String term, Long departmentId, String status, String designation,
                                      int page, int size, String sortBy, String sortDir);
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO getEmployeeByCode(String code);
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
    List<String> getUniqueDesignations();
    List<EmployeeDTO> globalSearch(String query);
}
