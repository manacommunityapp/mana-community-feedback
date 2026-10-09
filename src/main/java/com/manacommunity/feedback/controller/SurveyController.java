package com.manacommunity.feedback.controller;

import com.manacommunity.common.dto.ApiResponse;
import com.manacommunity.feedback.dto.CreateSurveyRequest;
import com.manacommunity.feedback.dto.SubmitSurveyResponseRequest;
import com.manacommunity.feedback.model.SurveyFormEntity;
import com.manacommunity.feedback.model.SurveyResponseEntity;
import com.manacommunity.feedback.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/feedback/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<ApiResponse<SurveyFormEntity>> createSurvey(
            @Valid @RequestBody CreateSurveyRequest request) {
        log.info("[API] Create survey for community={}", request.getCommunityId());
        SurveyFormEntity created = surveyService.createSurvey(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Survey created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SurveyFormEntity>> getSurveyById(@PathVariable String id) {
        SurveyFormEntity survey = surveyService.getSurveyById(id);
        return ResponseEntity.ok(ApiResponse.success("Survey retrieved successfully", survey));
    }

    @GetMapping("/community/{communityId}/active")
    public ResponseEntity<ApiResponse<List<SurveyFormEntity>>> getActiveSurveys(@PathVariable String communityId) {
        List<SurveyFormEntity> active = surveyService.getActiveSurveys(communityId);
        return ResponseEntity.ok(ApiResponse.success("Active surveys retrieved", active));
    }

    @PostMapping("/responses")
    public ResponseEntity<ApiResponse<SurveyResponseEntity>> submitResponse(
            @Valid @RequestBody SubmitSurveyResponseRequest request) {
        log.info("[API] Submit response for survey={}", request.getSurveyId());
        SurveyResponseEntity response = surveyService.submitResponse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Survey response submitted successfully", response));
    }

    @GetMapping("/{id}/responses")
    public ResponseEntity<ApiResponse<List<SurveyResponseEntity>>> getResponses(@PathVariable String id) {
        List<SurveyResponseEntity> responses = surveyService.getResponses(id);
        return ResponseEntity.ok(ApiResponse.success("Survey responses retrieved", responses));
    }
}
