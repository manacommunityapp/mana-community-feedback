package com.manacommunity.feedback.repository;

import com.manacommunity.feedback.model.SurveyResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponseEntity, String> {
    List<SurveyResponseEntity> findBySurveyId(String surveyId);
    List<SurveyResponseEntity> findBySurveyIdAndUserId(String surveyId, String userId);
    long countBySurveyId(String surveyId);
}
