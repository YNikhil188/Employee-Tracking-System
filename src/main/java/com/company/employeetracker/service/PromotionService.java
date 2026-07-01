package com.company.employeetracker.service;

import com.company.employeetracker.dto.PromotionRecommendationDTO;
import java.util.List;

public interface PromotionService {
    List<PromotionRecommendationDTO> getEligibleEmployees();
}
