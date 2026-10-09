package com.manacommunity.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitSurveyResponseRequest {

    @NotBlank(message = "surveyId is required")
    private String surveyId;

    @NotBlank(message = "communityId is required")
    private String communityId;

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "answersJson is required")
    private String answersJson;
}
