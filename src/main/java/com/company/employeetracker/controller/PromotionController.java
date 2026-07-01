package com.company.employeetracker.controller;

import com.company.employeetracker.dto.PromotionRecommendationDTO;
import com.company.employeetracker.service.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public ResponseEntity<List<PromotionRecommendationDTO>> getEligibleEmployees() {
        return ResponseEntity.ok(promotionService.getEligibleEmployees());
    }
}
