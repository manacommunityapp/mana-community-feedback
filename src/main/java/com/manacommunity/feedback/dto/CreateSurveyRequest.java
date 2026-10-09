package com.manacommunity.feedback.dto;

import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSurveyRequest {

    @NotBlank(message = "communityId is required")
    private String communityId;

    @NotBlank(message = "title is required")
    private String title;

    private String description;
    private FeedbackModuleType targetModule;
    private String questionsJson;
}
