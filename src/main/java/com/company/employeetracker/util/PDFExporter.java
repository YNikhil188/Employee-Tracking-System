package com.company.employeetracker.util;

import com.company.employeetracker.dto.DepartmentDTO;
import com.company.employeetracker.dto.EmployeeDTO;
import com.company.employeetracker.dto.PerformanceReviewDTO;
import com.company.employeetracker.dto.PromotionRecommendationDTO;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class PDFExporter {

    private static Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
    private static Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
    private static Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.BLACK);

    private static PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, headerFont));
        cell.setBackgroundColor(new Color(79, 70, 229)); // Brand indigo
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private static PdfPCell createDataCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, cellFont));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    public static byte[] exportEmployees(List<EmployeeDTO> employees) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();
        Paragraph title = new Paragraph("Employee Corporate Directory", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.0f, 2.0f, 2.5f, 2.0f, 2.0f, 1.2f, 1.0f, 1.5f});

        table.addCell(createHeaderCell("Code"));
        table.addCell(createHeaderCell("Name"));
        table.addCell(createHeaderCell("Email"));
        table.addCell(createHeaderCell("Department"));
        table.addCell(createHeaderCell("Designation"));
        table.addCell(createHeaderCell("Salary"));
        table.addCell(createHeaderCell("Status"));
        table.addCell(createHeaderCell("Joining Date"));

        for (EmployeeDTO e : employees) {
            table.addCell(createDataCell(e.getEmployeeCode(), Element.ALIGN_CENTER));
            table.addCell(createDataCell(e.getFirstName() + " " + e.getLastName(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(e.getEmail(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(e.getDepartmentName(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(e.getDesignation(), Element.ALIGN_LEFT));
            table.addCell(createDataCell("$" + String.format("%.2f", e.getSalary()), Element.ALIGN_RIGHT));
            table.addCell(createDataCell(e.getStatus(), Element.ALIGN_CENTER));
            table.addCell(createDataCell(e.getJoiningDate().toString(), Element.ALIGN_CENTER));
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    public static byte[] exportDepartments(List<DepartmentDTO> departments) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();
        Paragraph title = new Paragraph("Departments Overview", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{0.8f, 2.5f, 2.0f, 2.0f, 1.2f});

        table.addCell(createHeaderCell("ID"));
        table.addCell(createHeaderCell("Department Name"));
        table.addCell(createHeaderCell("Manager"));
        table.addCell(createHeaderCell("Location"));
        table.addCell(createHeaderCell("Employees Count"));

        for (DepartmentDTO d : departments) {
            table.addCell(createDataCell(d.getId().toString(), Element.ALIGN_CENTER));
            table.addCell(createDataCell(d.getName(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(d.getManager(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(d.getLocation(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(String.valueOf(d.getEmployeeCount()), Element.ALIGN_CENTER));
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    public static byte[] exportReviews(List<PerformanceReviewDTO> reviews) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();
        Paragraph title = new Paragraph("Performance Reviews Summary", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.0f, 2.5f, 2.0f, 1.2f, 1.2f, 1.5f, 1.5f});

        table.addCell(createHeaderCell("ID"));
        table.addCell(createHeaderCell("Employee Name"));
        table.addCell(createHeaderCell("Manager Reviewer"));
        table.addCell(createHeaderCell("Avg Score"));
        table.addCell(createHeaderCell("Rating"));
        table.addCell(createHeaderCell("Review Date"));
        table.addCell(createHeaderCell("Remarks"));

        for (PerformanceReviewDTO r : reviews) {
            table.addCell(createDataCell(r.getId().toString(), Element.ALIGN_CENTER));
            table.addCell(createDataCell(r.getEmployeeName(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(r.getManagerName(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(String.format("%.2f", r.getOverallScore()), Element.ALIGN_CENTER));
            table.addCell(createDataCell(r.getOverallRating(), Element.ALIGN_CENTER));
            table.addCell(createDataCell(r.getReviewDate().toString(), Element.ALIGN_CENTER));
            table.addCell(createDataCell(r.getRemarks() != null ? r.getRemarks() : "", Element.ALIGN_LEFT));
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    public static byte[] exportPromotions(List<PromotionRecommendationDTO> promotions) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();
        Paragraph title = new Paragraph("Promotion Recommendations", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.2f, 2.5f, 2.0f, 2.0f, 1.5f, 1.2f, 1.5f, 1.2f});

        table.addCell(createHeaderCell("Code"));
        table.addCell(createHeaderCell("Name"));
        table.addCell(createHeaderCell("Department"));
        table.addCell(createHeaderCell("Designation"));
        table.addCell(createHeaderCell("Experience (Yrs)"));
        table.addCell(createHeaderCell("Avg Score"));
        table.addCell(createHeaderCell("Avg Attendance"));
        table.addCell(createHeaderCell("Pending Goals"));

        for (PromotionRecommendationDTO p : promotions) {
            table.addCell(createDataCell(p.getEmployeeCode(), Element.ALIGN_CENTER));
            table.addCell(createDataCell(p.getEmployeeName(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(p.getDepartmentName(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(p.getDesignation(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(String.format("%.2f", p.getExperienceYears()), Element.ALIGN_CENTER));
            table.addCell(createDataCell(String.format("%.2f", p.getAverageScore()), Element.ALIGN_CENTER));
            table.addCell(createDataCell(String.format("%.2f", p.getAverageAttendance()) + "%", Element.ALIGN_CENTER));
            table.addCell(createDataCell(p.getPendingGoalsCount().toString(), Element.ALIGN_CENTER));
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }
}
