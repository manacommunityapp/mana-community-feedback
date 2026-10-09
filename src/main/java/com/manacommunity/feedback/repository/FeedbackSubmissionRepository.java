package com.manacommunity.feedback.repository;

import com.manacommunity.feedback.model.FeedbackSubmissionEntity;
import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.model.enums.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackSubmissionRepository extends JpaRepository<FeedbackSubmissionEntity, String> {
    List<FeedbackSubmissionEntity> findByCommunityId(String communityId);
    List<FeedbackSubmissionEntity> findByCommunityIdAndModuleType(String communityId, FeedbackModuleType moduleType);
    List<FeedbackSubmissionEntity> findByUserId(String userId);
    List<FeedbackSubmissionEntity> findByReferenceId(String referenceId);
    List<FeedbackSubmissionEntity> findByCommunityIdAndStatus(String communityId, FeedbackStatus status);
}
