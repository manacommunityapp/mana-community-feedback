package com.manacommunity.feedback.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "survey_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponseEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, length = 64)
    private String surveyId;

    @Column(nullable = false, length = 64)
    private String communityId;

    @Column(nullable = false, length = 64)
    private String userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answersJson;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    public void prePersist() {
        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
    }
}
