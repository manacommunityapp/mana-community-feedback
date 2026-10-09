package com.manacommunity.feedback.controller;

import com.manacommunity.common.dto.ApiResponse;
import com.manacommunity.feedback.dto.FeedbackAnalyticsSummary;
import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.service.FeedbackAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/feedback/analytics")
@RequiredArgsConstructor
public class FeedbackAnalyticsController {

    private final FeedbackAnalyticsService analyticsService;

    @GetMapping("/community/{communityId}")
    public ResponseEntity<ApiResponse<FeedbackAnalyticsSummary>> getCommunityAnalytics(
            @PathVariable String communityId,
            @RequestParam(required = false) FeedbackModuleType module) {
        log.info("[API] Feedback analytics for community={} module={}", communityId, module);
        FeedbackAnalyticsSummary summary = analyticsService.getAnalytics(communityId, module);
        return ResponseEntity.ok(ApiResponse.success("Analytics retrieved successfully", summary));
    }
}
