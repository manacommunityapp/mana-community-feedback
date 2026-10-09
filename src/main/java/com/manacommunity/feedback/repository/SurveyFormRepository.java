package com.manacommunity.feedback.repository;

import com.manacommunity.feedback.model.SurveyFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyFormRepository extends JpaRepository<SurveyFormEntity, String> {
    List<SurveyFormEntity> findByCommunityIdAndActiveTrue(String communityId);
    List<SurveyFormEntity> findByCommunityId(String communityId);
}
