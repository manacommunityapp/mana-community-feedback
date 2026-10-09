package com.manacommunity.feedback;

import com.manacommunity.feedback.dto.SubmitFeedbackRequest;
import com.manacommunity.feedback.dto.UpdateFeedbackStatusRequest;
import com.manacommunity.feedback.model.FeedbackSubmissionEntity;
import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.model.enums.FeedbackStatus;
import com.manacommunity.feedback.model.enums.SentimentType;
import com.manacommunity.feedback.repository.FeedbackSubmissionRepository;
import com.manacommunity.feedback.service.FeedbackSubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FeedbackSubmissionServiceTest {

    @Autowired
    private FeedbackSubmissionService feedbackService;

    @Autowired
    private FeedbackSubmissionRepository feedbackRepository;

    @BeforeEach
    void setup() {
        feedbackRepository.deleteAll();
    }

    @Test
    void testSubmitFeedbackPositiveSentiment() {
        SubmitFeedbackRequest request = SubmitFeedbackRequest.builder()
                .communityId("comm-100")
                .userId("user-01")
                .moduleType(FeedbackModuleType.FOOD)
                .referenceId("order-555")
                .rating(5)
                .title("Great dinner")
                .comment("The food was delicious and delivery was fast!")
                .build();

        FeedbackSubmissionEntity saved = feedbackService.submitFeedback(request);

        assertNotNull(saved.getId());
        assertEquals(SentimentType.POSITIVE, saved.getSentiment());
        assertEquals(FeedbackStatus.SUBMITTED, saved.getStatus());
        assertEquals("comm-100", saved.getCommunityId());
    }

    @Test
    void testSubmitFeedbackNegativeSentiment() {
        SubmitFeedbackRequest request = SubmitFeedbackRequest.builder()
                .communityId("comm-100")
                .userId("user-02")
                .moduleType(FeedbackModuleType.HELPDESK)
                .referenceId("ticket-888")
                .rating(1)
                .title("Terrible resolution")
                .comment("Extremely bad experience, slow response and rude staff.")
                .build();

        FeedbackSubmissionEntity saved = feedbackService.submitFeedback(request);

        assertNotNull(saved.getId());
        assertEquals(SentimentType.NEGATIVE, saved.getSentiment());
    }

    @Test
    void testUpdateStatus() {
        SubmitFeedbackRequest request = SubmitFeedbackRequest.builder()
                .communityId("comm-100")
                .userId("user-03")
                .moduleType(FeedbackModuleType.AMENITIES)
                .rating(4)
                .comment("Good swimming pool maintenance")
                .build();

        FeedbackSubmissionEntity saved = feedbackService.submitFeedback(request);

        UpdateFeedbackStatusRequest updateReq = UpdateFeedbackStatusRequest.builder()
                .status(FeedbackStatus.RESOLVED)
                .resolutionNotes("Acknowledged and cleaned.")
                .build();

        FeedbackSubmissionEntity updated = feedbackService.updateStatus(saved.getId(), updateReq);

        assertEquals(FeedbackStatus.RESOLVED, updated.getStatus());
        assertEquals("Acknowledged and cleaned.", updated.getResolutionNotes());
    }
}
