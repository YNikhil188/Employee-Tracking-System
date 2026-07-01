package com.company.employeetracker.util;

import com.company.employeetracker.dto.DepartmentDTO;
import com.company.employeetracker.dto.EmployeeDTO;
import com.company.employeetracker.dto.PerformanceReviewDTO;
import com.company.employeetracker.dto.PromotionRecommendationDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    public static byte[] exportEmployees(List<EmployeeDTO> employees) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Employees");
            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] columns = {"Code", "First Name", "Last Name", "Email", "Phone", "Designation", "Department", "Salary", "Status", "Joining Date"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (EmployeeDTO e : employees) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(e.getEmployeeCode());
                row.createCell(1).setCellValue(e.getFirstName());
                row.createCell(2).setCellValue(e.getLastName());
                row.createCell(3).setCellValue(e.getEmail());
                row.createCell(4).setCellValue(e.getPhone() != null ? e.getPhone() : "");
                row.createCell(5).setCellValue(e.getDesignation());
                row.createCell(6).setCellValue(e.getDepartmentName() != null ? e.getDepartmentName() : "");
                
                Cell salaryCell = row.createCell(7);
                salaryCell.setCellValue(e.getSalary());
                
                row.createCell(8).setCellValue(e.getStatus());
                row.createCell(9).setCellValue(e.getJoiningDate().toString());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public static byte[] exportDepartments(List<DepartmentDTO> departments) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Departments");
            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] columns = {"ID", "Department Name", "Manager Name", "Location", "Description", "Employees Count"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (DepartmentDTO d : departments) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(d.getId());
                row.createCell(1).setCellValue(d.getName());
                row.createCell(2).setCellValue(d.getManager());
                row.createCell(3).setCellValue(d.getLocation());
                row.createCell(4).setCellValue(d.getDescription() != null ? d.getDescription() : "");
                row.createCell(5).setCellValue(d.getEmployeeCount());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public static byte[] exportReviews(List<PerformanceReviewDTO> reviews) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Reviews");
            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] columns = {"ID", "Employee Name", "Manager Reviewer", "Technical", "Communication", "Teamwork", "Leadership", "Attendance", "Problem Solving", "Innovation", "Discipline", "Overall Score", "Rating", "Review Date"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (PerformanceReviewDTO r : reviews) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.getId());
                row.createCell(1).setCellValue(r.getEmployeeName());
                row.createCell(2).setCellValue(r.getManagerName());
                row.createCell(3).setCellValue(r.getTechnicalKnowledge());
                row.createCell(4).setCellValue(r.getCommunication());
                row.createCell(5).setCellValue(r.getTeamwork());
                row.createCell(6).setCellValue(r.getLeadership());
                row.createCell(7).setCellValue(r.getAttendance());
                row.createCell(8).setCellValue(r.getProblemSolving());
                row.createCell(9).setCellValue(r.getInnovation());
                row.createCell(10).setCellValue(r.getDiscipline());
                row.createCell(11).setCellValue(r.getOverallScore());
                row.createCell(12).setCellValue(r.getOverallRating());
                row.createCell(13).setCellValue(r.getReviewDate().toString());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public static byte[] exportPromotions(List<PromotionRecommendationDTO> promotions) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Promotions");
            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] columns = {"Code", "Name", "Department", "Designation", "Experience (Years)", "Avg Score", "Avg Attendance", "Pending Goals"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (PromotionRecommendationDTO p : promotions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getEmployeeCode());
                row.createCell(1).setCellValue(p.getEmployeeName());
                row.createCell(2).setCellValue(p.getDepartmentName());
                row.createCell(3).setCellValue(p.getDesignation());
                row.createCell(4).setCellValue(p.getExperienceYears());
                row.createCell(5).setCellValue(p.getAverageScore());
                row.createCell(6).setCellValue(p.getAverageAttendance());
                row.createCell(7).setCellValue(p.getPendingGoalsCount());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
