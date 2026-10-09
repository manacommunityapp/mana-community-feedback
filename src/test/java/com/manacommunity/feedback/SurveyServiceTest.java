package com.manacommunity.feedback;

import com.manacommunity.feedback.dto.CreateSurveyRequest;
import com.manacommunity.feedback.dto.SubmitSurveyResponseRequest;
import com.manacommunity.feedback.model.SurveyFormEntity;
import com.manacommunity.feedback.model.SurveyResponseEntity;
import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.repository.SurveyFormRepository;
import com.manacommunity.feedback.repository.SurveyResponseRepository;
import com.manacommunity.feedback.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SurveyFormRepository surveyFormRepository;

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    @BeforeEach
    void setup() {
        surveyResponseRepository.deleteAll();
        surveyFormRepository.deleteAll();
    }

    @Test
    void testSurveyCreationAndResponseSubmission() {
        CreateSurveyRequest req = CreateSurveyRequest.builder()
                .communityId("comm-200")
                .title("Annual Resident Satisfaction Survey")
                .description("Please share your thoughts on community services.")
                .targetModule(FeedbackModuleType.PLATFORM_APP)
                .questionsJson("[{\"id\":1, \"question\":\"How satisfied are you?\", \"type\":\"rating\"}]")
                .build();

        SurveyFormEntity created = surveyService.createSurvey(req);
        assertNotNull(created.getId());
        assertTrue(created.isActive());

        List<SurveyFormEntity> active = surveyService.getActiveSurveys("comm-200");
        assertEquals(1, active.size());

        SubmitSurveyResponseRequest responseReq = SubmitSurveyResponseRequest.builder()
                .surveyId(created.getId())
                .communityId("comm-200")
                .userId("resident-99")
                .answersJson("{\"1\": 5}")
                .build();

        SurveyResponseEntity response = surveyService.submitResponse(responseReq);
        assertNotNull(response.getId());
        assertEquals("resident-99", response.getUserId());

        List<SurveyResponseEntity> responses = surveyService.getResponses(created.getId());
        assertEquals(1, responses.size());
    }
}
