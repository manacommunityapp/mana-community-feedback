package com.manacommunity.feedback.model;

import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.model.enums.FeedbackStatus;
import com.manacommunity.feedback.model.enums.FeedbackType;
import com.manacommunity.feedback.model.enums.SentimentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback_submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackSubmissionEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, length = 64)
    private String communityId;

    @Column(nullable = false, length = 64)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private FeedbackModuleType moduleType;

    @Column(length = 64)
    private String referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private FeedbackType feedbackType;

    @Column(nullable = false)
    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private SentimentType sentiment;

    @Column(length = 256)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(length = 256)
    private String categoryTags;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private FeedbackStatus status;

    @Column(columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (status == null) {
            status = FeedbackStatus.SUBMITTED;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
