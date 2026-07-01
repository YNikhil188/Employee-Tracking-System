package com.company.employeetracker.service.impl;

import com.company.employeetracker.dto.DepartmentDTO;
import com.company.employeetracker.dto.EmployeeDTO;
import com.company.employeetracker.dto.PerformanceReviewDTO;
import com.company.employeetracker.dto.PromotionRecommendationDTO;
import com.company.employeetracker.entity.Department;
import com.company.employeetracker.entity.Employee;
import com.company.employeetracker.entity.PerformanceReview;
import com.company.employeetracker.repository.DepartmentRepository;
import com.company.employeetracker.repository.EmployeeRepository;
import com.company.employeetracker.repository.PerformanceReviewRepository;
import com.company.employeetracker.service.PromotionService;
import com.company.employeetracker.service.ReportService;
import com.company.employeetracker.util.CSVExporter;
import com.company.employeetracker.util.ExcelExporter;
import com.company.employeetracker.util.PDFExporter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PerformanceReviewRepository reviewRepository;
    private final PromotionService promotionService;
    private final ModelMapper modelMapper;

    public ReportServiceImpl(EmployeeRepository employeeRepository,
                             DepartmentRepository departmentRepository,
                             PerformanceReviewRepository reviewRepository,
                             PromotionService promotionService,
                             ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.reviewRepository = reviewRepository;
        this.promotionService = promotionService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateEmployeeReport(String format) throws IOException {
        log.info("Generating Employee Report in format: {}", format);
        List<EmployeeDTO> dtos = employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return switch (format.toLowerCase()) {
            case "csv" -> CSVExporter.exportEmployees(dtos);
            case "excel" -> ExcelExporter.exportEmployees(dtos);
            case "pdf" -> PDFExporter.exportEmployees(dtos);
            default -> throw new IllegalArgumentException("Unsupported report format: " + format);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateDepartmentReport(String format) throws IOException {
        log.info("Generating Department Report in format: {}", format);
        List<DepartmentDTO> dtos = departmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return switch (format.toLowerCase()) {
            case "csv" -> CSVExporter.exportDepartments(dtos);
            case "excel" -> ExcelExporter.exportDepartments(dtos);
            case "pdf" -> PDFExporter.exportDepartments(dtos);
            default -> throw new IllegalArgumentException("Unsupported report format: " + format);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generatePerformanceReport(String format) throws IOException {
        log.info("Generating Performance Report in format: {}", format);
        List<PerformanceReviewDTO> dtos = reviewRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return switch (format.toLowerCase()) {
            case "csv" -> CSVExporter.exportReviews(dtos);
            case "excel" -> ExcelExporter.exportReviews(dtos);
            case "pdf" -> PDFExporter.exportReviews(dtos);
            default -> throw new IllegalArgumentException("Unsupported report format: " + format);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generatePromotionReport(String format) throws IOException {
        log.info("Generating Promotion Report in format: {}", format);
        List<PromotionRecommendationDTO> dtos = promotionService.getEligibleEmployees();

        return switch (format.toLowerCase()) {
            case "csv" -> CSVExporter.exportPromotions(dtos);
            case "excel" -> ExcelExporter.exportPromotions(dtos);
            case "pdf" -> PDFExporter.exportPromotions(dtos);
            default -> throw new IllegalArgumentException("Unsupported report format: " + format);
        };
    }

    private EmployeeDTO convertToDto(Employee employee) {
        EmployeeDTO dto = modelMapper.map(employee, EmployeeDTO.class);
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        return dto;
    }

    private DepartmentDTO convertToDto(Department department) {
        DepartmentDTO dto = modelMapper.map(department, DepartmentDTO.class);
        dto.setEmployeeCount((int) employeeRepository.countByDepartmentId(department.getId()));
        return dto;
    }

    private PerformanceReviewDTO convertToDto(PerformanceReview review) {
        PerformanceReviewDTO dto = modelMapper.map(review, PerformanceReviewDTO.class);
        if (review.getEmployee() != null) {
            dto.setEmployeeId(review.getEmployee().getId());
            dto.setEmployeeName(review.getEmployee().getFirstName() + " " + review.getEmployee().getLastName());
        }
        if (review.getManager() != null) {
            dto.setManagerId(review.getManager().getId());
            dto.setManagerName(review.getManager().getUsername());
        }
        return dto;
    }
}
