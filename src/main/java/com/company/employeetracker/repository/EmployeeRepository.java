package com.company.employeetracker.repository;

import com.company.employeetracker.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id);

    long countByDepartmentId(Long departmentId);

    List<Employee> findByDepartmentId(Long departmentId);

    // Dynamic search and filter query
    @Query("SELECT e FROM Employee e WHERE " +
           "(:term IS NULL OR :term = '' OR " +
           " LOWER(e.firstName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           " LOWER(e.lastName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           " LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           " LOWER(e.designation) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           " LOWER(e.department.name) LIKE LOWER(CONCAT('%', :term, '%'))) AND " +
           "(:departmentId IS NULL OR e.department.id = :departmentId) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:designation IS NULL OR e.designation = :designation)")
    Page<Employee> findWithFilters(@Param("term") String term, 
                                   @Param("departmentId") Long departmentId, 
                                   @Param("status") String status, 
                                   @Param("designation") String designation, 
                                   Pageable pageable);

    // Global Search (single search box matching anything)
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.designation) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.department.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Employee> globalSearch(@Param("query") String query);

    @Query("SELECT DISTINCT e.designation FROM Employee e")
    List<String> findUniqueDesignations();
}
