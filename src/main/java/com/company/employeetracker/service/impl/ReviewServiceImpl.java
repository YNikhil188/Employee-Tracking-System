package com.company.employeetracker.service.impl;

import com.company.employeetracker.dto.PerformanceReviewDTO;
import com.company.employeetracker.entity.Employee;
import com.company.employeetracker.entity.PerformanceReview;
import com.company.employeetracker.entity.User;
import com.company.employeetracker.exception.EmployeeNotFoundException;
import com.company.employeetracker.repository.EmployeeRepository;
import com.company.employeetracker.repository.PerformanceReviewRepository;
import com.company.employeetracker.repository.UserRepository;
import com.company.employeetracker.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ReviewServiceImpl(PerformanceReviewRepository reviewRepository,
                             EmployeeRepository employeeRepository,
                             UserRepository userRepository,
                             ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public PerformanceReviewDTO submitReview(PerformanceReviewDTO dto, String managerUsername) {
        log.info("Submitting performance review for employee id: {} by manager: {}", 
                dto.getEmployeeId(), managerUsername);

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        User manager = userRepository.findByUsername(managerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Manager user not found: " + managerUsername));

        // Calculate Overall Score (Average of 8 metrics)
        double total = dto.getTechnicalKnowledge() 
                     + dto.getCommunication() 
                     + dto.getTeamwork() 
                     + dto.getLeadership() 
                     + dto.getAttendance() 
                     + dto.getProblemSolving() 
                     + dto.getInnovation() 
                     + dto.getDiscipline();
        
        double overallScore = total / 8.0;

        // Determine Rating Category
        String overallRating;
        if (overallScore >= 90.0) {
            overallRating = "Excellent";
        } else if (overallScore >= 80.0) {
            overallRating = "Very Good";
        } else if (overallScore >= 70.0) {
            overallRating = "Good";
        } else if (overallScore >= 50.0) {
            overallRating = "Average";
        } else {
            overallRating = "Needs Improvement";
        }

        PerformanceReview review = PerformanceReview.builder()
                .employee(employee)
                .manager(manager)
                .technicalKnowledge(dto.getTechnicalKnowledge())
                .communication(dto.getCommunication())
                .teamwork(dto.getTeamwork())
                .leadership(dto.getLeadership())
                .attendance(dto.getAttendance())
                .problemSolving(dto.getProblemSolving())
                .innovation(dto.getInnovation())
                .discipline(dto.getDiscipline())
                .remarks(dto.getRemarks())
                .reviewDate(LocalDate.now())
                .overallScore(overallScore)
                .overallRating(overallRating)
                .build();

        PerformanceReview saved = reviewRepository.save(review);
        return convertToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getEmployeeReviews(Long employeeId) {
        log.info("Fetching review history for employee id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        return reviewRepository.findByEmployeeIdOrderByReviewDateDesc(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageScore(Long employeeId) {
        log.info("Fetching average score for employee id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        Double avg = reviewRepository.getAverageScoreByEmployeeId(employeeId);
        return avg != null ? avg : 0.0;
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
