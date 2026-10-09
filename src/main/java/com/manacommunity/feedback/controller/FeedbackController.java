package com.manacommunity.feedback.controller;

import com.manacommunity.common.dto.ApiResponse;
import com.manacommunity.feedback.dto.SubmitFeedbackRequest;
import com.manacommunity.feedback.dto.UpdateFeedbackStatusRequest;
import com.manacommunity.feedback.model.FeedbackSubmissionEntity;
import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.service.FeedbackSubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/feedback/submissions")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackSubmissionService feedbackService;

    @PostMapping
    public ResponseEntity<ApiResponse<FeedbackSubmissionEntity>> submitFeedback(
            @Valid @RequestBody SubmitFeedbackRequest request) {
        log.info("[API] Submit feedback for community={} module={}", request.getCommunityId(), request.getModuleType());
        FeedbackSubmissionEntity saved = feedbackService.submitFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback submitted successfully", saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedbackSubmissionEntity>> getFeedbackById(@PathVariable String id) {
        FeedbackSubmissionEntity entity = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(ApiResponse.success("Feedback retrieved successfully", entity));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<FeedbackSubmissionEntity>> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateFeedbackStatusRequest request) {
        log.info("[API] Update feedback status for id={} to {}", id, request.getStatus());
        FeedbackSubmissionEntity updated = feedbackService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Feedback status updated successfully", updated));
    }

    @GetMapping("/community/{communityId}")
    public ResponseEntity<ApiResponse<List<FeedbackSubmissionEntity>>> getByCommunity(
            @PathVariable String communityId,
            @RequestParam(required = false) FeedbackModuleType module) {
        List<FeedbackSubmissionEntity> list = feedbackService.getFeedbackByCommunity(communityId, module);
        return ResponseEntity.ok(ApiResponse.success("Community feedback retrieved", list));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<FeedbackSubmissionEntity>>> getByUser(@PathVariable String userId) {
        List<FeedbackSubmissionEntity> list = feedbackService.getFeedbackByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User feedback retrieved", list));
    }

    @GetMapping("/reference/{referenceId}")
    public ResponseEntity<ApiResponse<List<FeedbackSubmissionEntity>>> getByReference(@PathVariable String referenceId) {
        List<FeedbackSubmissionEntity> list = feedbackService.getFeedbackByReference(referenceId);
        return ResponseEntity.ok(ApiResponse.success("Reference feedback retrieved", list));
    }
}
