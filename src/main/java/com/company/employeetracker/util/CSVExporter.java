package com.company.employeetracker.util;

import com.company.employeetracker.dto.DepartmentDTO;
import com.company.employeetracker.dto.EmployeeDTO;
import com.company.employeetracker.dto.PerformanceReviewDTO;
import com.company.employeetracker.dto.PromotionRecommendationDTO;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class CSVExporter {

    public static byte[] exportEmployees(List<EmployeeDTO> employees) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Employee Code,First Name,Last Name,Email,Phone,Designation,Department,Salary,Status,Joining Date");
            for (EmployeeDTO e : employees) {
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\"\n",
                        e.getEmployeeCode(), e.getFirstName(), e.getLastName(), e.getEmail(),
                        e.getPhone() != null ? e.getPhone() : "", e.getDesignation(),
                        e.getDepartmentName() != null ? e.getDepartmentName() : "", e.getSalary(),
                        e.getStatus(), e.getJoiningDate());
            }
            writer.flush();
        }
        return out.toByteArray();
    }

    public static byte[] exportDepartments(List<DepartmentDTO> departments) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("ID,Department Name,Manager Name,Location,Description,Employees Count");
            for (DepartmentDTO d : departments) {
                writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",%d\n",
                        d.getId(), d.getName(), d.getManager(), d.getLocation(),
                        d.getDescription() != null ? d.getDescription() : "", d.getEmployeeCount());
            }
            writer.flush();
        }
        return out.toByteArray();
    }

    public static byte[] exportReviews(List<PerformanceReviewDTO> reviews) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Review ID,Employee Name,Reviewer Manager,Technical,Communication,Teamwork,Leadership,Attendance,Problem Solving,Innovation,Discipline,Overall Score,Rating,Review Date");
            for (PerformanceReviewDTO r : reviews) {
                writer.printf("%d,\"%s\",\"%s\",%d,%d,%d,%d,%d,%d,%d,%d,%.2f,\"%s\",\"%s\"\n",
                        r.getId(), r.getEmployeeName(), r.getManagerName(),
                        r.getTechnicalKnowledge(), r.getCommunication(), r.getTeamwork(),
                        r.getLeadership(), r.getAttendance(), r.getProblemSolving(),
                        r.getInnovation(), r.getDiscipline(), r.getOverallScore(),
                        r.getOverallRating(), r.getReviewDate());
            }
            writer.flush();
        }
        return out.toByteArray();
    }

    public static byte[] exportPromotions(List<PromotionRecommendationDTO> promotions) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Employee Code,Employee Name,Department,Designation,Experience (Years),Avg Score,Avg Attendance,Pending Goals");
            for (PromotionRecommendationDTO p : promotions) {
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",%.2f,%.2f,%.2f,%d\n",
                        p.getEmployeeCode(), p.getEmployeeName(), p.getDepartmentName(),
                        p.getDesignation(), p.getExperienceYears(), p.getAverageScore(),
                        p.getAverageAttendance(), p.getPendingGoalsCount());
            }
            writer.flush();
        }
        return out.toByteArray();
    }
}
