package com.company.employeetracker.service;

import java.io.IOException;

public interface ReportService {
    byte[] generateEmployeeReport(String format) throws IOException;
    byte[] generateDepartmentReport(String format) throws IOException;
    byte[] generatePerformanceReport(String format) throws IOException;
    byte[] generatePromotionReport(String format) throws IOException;
}
