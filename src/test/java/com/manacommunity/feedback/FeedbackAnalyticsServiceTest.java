package com.manacommunity.feedback;

import com.manacommunity.feedback.dto.FeedbackAnalyticsSummary;
import com.manacommunity.feedback.dto.SubmitFeedbackRequest;
import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.repository.FeedbackSubmissionRepository;
import com.manacommunity.feedback.service.FeedbackAnalyticsService;
import com.manacommunity.feedback.service.FeedbackSubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FeedbackAnalyticsServiceTest {

    @Autowired
    private FeedbackAnalyticsService analyticsService;

    @Autowired
    private FeedbackSubmissionService feedbackService;

    @Autowired
    private FeedbackSubmissionRepository feedbackRepository;

    @BeforeEach
    void setup() {
        feedbackRepository.deleteAll();
    }

    @Test
    void testAnalyticsCalculation() {
        // user 1: 5 stars (promoter / satisfied)
        feedbackService.submitFeedback(SubmitFeedbackRequest.builder()
                .communityId("comm-analytics")
                .userId("u1")
                .moduleType(FeedbackModuleType.EVENTS)
                .rating(5)
                .comment("Excellent event")
                .build());

        // user 2: 4 stars (promoter / satisfied)
        feedbackService.submitFeedback(SubmitFeedbackRequest.builder()
                .communityId("comm-analytics")
                .userId("u2")
                .moduleType(FeedbackModuleType.EVENTS)
                .rating(4)
                .comment("Good event")
                .build());

        // user 3: 2 stars (detractor / unsatisfied)
        feedbackService.submitFeedback(SubmitFeedbackRequest.builder()
                .communityId("comm-analytics")
                .userId("u3")
                .moduleType(FeedbackModuleType.EVENTS)
                .rating(2)
                .comment("Bad sound quality")
                .build());

        FeedbackAnalyticsSummary summary = analyticsService.getAnalytics("comm-analytics", FeedbackModuleType.EVENTS);

        assertEquals(3, summary.getTotalSubmissions());
        // Avg = (5 + 4 + 2) / 3 = 3.67
        assertEquals(3.67, summary.getAverageRating());
        // CSAT = 2 / 3 = 66.67%
        assertEquals(66.67, summary.getCsatPercentage());
        // NPS = (2 promoters - 1 detractor) / 3 = 33.33%
        assertEquals(33.33, summary.getNpsScore());
        assertEquals(2, summary.getPositiveSentimentCount());
        assertEquals(1, summary.getNegativeSentimentCount());
    }
}
