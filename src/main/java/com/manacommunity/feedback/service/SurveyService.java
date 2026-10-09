package com.manacommunity.feedback.service;

import com.manacommunity.feedback.dto.CreateSurveyRequest;
import com.manacommunity.feedback.dto.SubmitSurveyResponseRequest;
import com.manacommunity.feedback.model.SurveyFormEntity;
import com.manacommunity.feedback.model.SurveyResponseEntity;
import com.manacommunity.feedback.repository.SurveyFormRepository;
import com.manacommunity.feedback.repository.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {

    private final SurveyFormRepository surveyFormRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    @Transactional
    public SurveyFormEntity createSurvey(CreateSurveyRequest req) {
        log.info("Creating survey for community={}, title={}", req.getCommunityId(), req.getTitle());

        SurveyFormEntity entity = SurveyFormEntity.builder()
                .id(UUID.randomUUID().toString())
                .communityId(req.getCommunityId())
                .title(req.getTitle())
                .description(req.getDescription())
                .targetModule(req.getTargetModule())
                .active(true)
                .questionsJson(req.getQuestionsJson() != null ? req.getQuestionsJson() : "[]")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return surveyFormRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<SurveyFormEntity> getActiveSurveys(String communityId) {
        return surveyFormRepository.findByCommunityIdAndActiveTrue(communityId);
    }

    @Transactional(readOnly = true)
    public SurveyFormEntity getSurveyById(String id) {
        return surveyFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found with id: " + id));
    }

    @Transactional
    public SurveyResponseEntity submitResponse(SubmitSurveyResponseRequest req) {
        SurveyFormEntity survey = getSurveyById(req.getSurveyId());
        if (!survey.isActive()) {
            throw new IllegalStateException("Survey is no longer active");
        }

        SurveyResponseEntity entity = SurveyResponseEntity.builder()
                .id(UUID.randomUUID().toString())
                .surveyId(req.getSurveyId())
                .communityId(req.getCommunityId())
                .userId(req.getUserId())
                .answersJson(req.getAnswersJson())
                .submittedAt(LocalDateTime.now())
                .build();

        log.info("Submitted survey response for surveyId={}, userId={}", req.getSurveyId(), req.getUserId());
        return surveyResponseRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<SurveyResponseEntity> getResponses(String surveyId) {
        return surveyResponseRepository.findBySurveyId(surveyId);
    }
}
