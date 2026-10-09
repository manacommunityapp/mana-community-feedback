package com.manacommunity.feedback.service;

import com.manacommunity.feedback.dto.SubmitFeedbackRequest;
import com.manacommunity.feedback.dto.UpdateFeedbackStatusRequest;
import com.manacommunity.feedback.model.FeedbackSubmissionEntity;
import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.model.enums.FeedbackStatus;
import com.manacommunity.feedback.model.enums.FeedbackType;
import com.manacommunity.feedback.model.enums.SentimentType;
import com.manacommunity.feedback.repository.FeedbackSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackSubmissionService {

    private final FeedbackSubmissionRepository feedbackRepository;

    private static final Set<String> POSITIVE_KEYWORDS = Set.of(
            "great", "excellent", "amazing", "good", "awesome", "perfect",
            "superb", "fast", "helpful", "friendly", "clean", "wonderful"
    );

    private static final Set<String> NEGATIVE_KEYWORDS = Set.of(
            "bad", "terrible", "worst", "awful", "horrible", "slow",
            "broken", "dirty", "rude", "poor", "unacceptable", "delay"
    );

    @Transactional
    public FeedbackSubmissionEntity submitFeedback(SubmitFeedbackRequest req) {
        log.info("Submitting feedback for community={}, user={}, module={}",
                req.getCommunityId(), req.getUserId(), req.getModuleType());

        FeedbackType fType = req.getFeedbackType();
        if (fType == null) {
            fType = req.getRating() > 5 ? FeedbackType.NPS_0_10 : FeedbackType.CSAT_1_5;
        }

        SentimentType sentiment = inferSentiment(req.getRating(), fType, req.getComment());

        FeedbackSubmissionEntity entity = FeedbackSubmissionEntity.builder()
                .id(UUID.randomUUID().toString())
                .communityId(req.getCommunityId())
                .userId(req.getUserId())
                .moduleType(req.getModuleType())
                .referenceId(req.getReferenceId())
                .feedbackType(fType)
                .rating(req.getRating())
                .sentiment(sentiment)
                .title(req.getTitle())
                .comment(req.getComment())
                .categoryTags(req.getCategoryTags())
                .status(FeedbackStatus.SUBMITTED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return feedbackRepository.save(entity);
    }

    @Transactional
    public FeedbackSubmissionEntity updateStatus(String feedbackId, UpdateFeedbackStatusRequest req) {
        FeedbackSubmissionEntity entity = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with id: " + feedbackId));

        entity.setStatus(req.getStatus());
        if (req.getResolutionNotes() != null) {
            entity.setResolutionNotes(req.getResolutionNotes());
        }
        entity.setUpdatedAt(LocalDateTime.now());

        log.info("Updated feedback status for id={} to {}", feedbackId, req.getStatus());
        return feedbackRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public FeedbackSubmissionEntity getFeedbackById(String id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<FeedbackSubmissionEntity> getFeedbackByCommunity(String communityId, FeedbackModuleType moduleType) {
        if (moduleType != null) {
            return feedbackRepository.findByCommunityIdAndModuleType(communityId, moduleType);
        }
        return feedbackRepository.findByCommunityId(communityId);
    }

    @Transactional(readOnly = true)
    public List<FeedbackSubmissionEntity> getFeedbackByUser(String userId) {
        return feedbackRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<FeedbackSubmissionEntity> getFeedbackByReference(String referenceId) {
        return feedbackRepository.findByReferenceId(referenceId);
    }

    public SentimentType inferSentiment(Integer rating, FeedbackType type, String comment) {
        int textScore = 0;
        if (comment != null && !comment.isBlank()) {
            String lower = comment.toLowerCase(Locale.ROOT);
            for (String pos : POSITIVE_KEYWORDS) {
                if (lower.contains(pos)) textScore++;
            }
            for (String neg : NEGATIVE_KEYWORDS) {
                if (lower.contains(neg)) textScore--;
            }
        }

        if (type == FeedbackType.NPS_0_10) {
            if (rating >= 9) return textScore < -1 ? SentimentType.NEUTRAL : SentimentType.POSITIVE;
            if (rating <= 6) return textScore > 1 ? SentimentType.NEUTRAL : SentimentType.NEGATIVE;
            return textScore > 0 ? SentimentType.POSITIVE : (textScore < 0 ? SentimentType.NEGATIVE : SentimentType.NEUTRAL);
        } else {
            if (rating >= 4) return textScore < -1 ? SentimentType.NEUTRAL : SentimentType.POSITIVE;
            if (rating <= 2) return textScore > 1 ? SentimentType.NEUTRAL : SentimentType.NEGATIVE;
            return textScore > 0 ? SentimentType.POSITIVE : (textScore < 0 ? SentimentType.NEGATIVE : SentimentType.NEUTRAL);
        }
    }
}
