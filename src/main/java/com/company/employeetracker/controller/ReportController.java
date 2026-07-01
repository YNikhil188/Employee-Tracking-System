package com.company.employeetracker.controller;

import com.company.employeetracker.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    private ResponseEntity<byte[]> buildResponse(byte[] data, String filename, String format) {
        HttpHeaders headers = new HttpHeaders();
        String fullFilename = filename + "_" + System.currentTimeMillis() + "." + (format.equalsIgnoreCase("excel") ? "xlsx" : format.toLowerCase());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fullFilename);

        MediaType mediaType = switch (format.toLowerCase()) {
            case "csv" -> MediaType.parseMediaType("text/csv");
            case "excel" -> MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case "pdf" -> MediaType.APPLICATION_PDF;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .body(data);
    }

    @GetMapping("/employees/{format}")
    public ResponseEntity<byte[]> downloadEmployeeReport(@PathVariable String format) throws IOException {
        byte[] reportData = reportService.generateEmployeeReport(format);
        return buildResponse(reportData, "employees_report", format);
    }

    @GetMapping("/departments/{format}")
    public ResponseEntity<byte[]> downloadDepartmentReport(@PathVariable String format) throws IOException {
        byte[] reportData = reportService.generateDepartmentReport(format);
        return buildResponse(reportData, "departments_report", format);
    }

    @GetMapping("/performance/{format}")
    public ResponseEntity<byte[]> downloadPerformanceReport(@PathVariable String format) throws IOException {
        byte[] reportData = reportService.generatePerformanceReport(format);
        return buildResponse(reportData, "performance_report", format);
    }

    @GetMapping("/promotions/{format}")
    public ResponseEntity<byte[]> downloadPromotionReport(@PathVariable String format) throws IOException {
        byte[] reportData = reportService.generatePromotionReport(format);
        return buildResponse(reportData, "promotions_report", format);
    }
}
