package com.manacommunity.feedback.dto;

import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.model.enums.FeedbackType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitFeedbackRequest {

    @NotBlank(message = "communityId is required")
    private String communityId;

    @NotBlank(message = "userId is required")
    private String userId;

    @NotNull(message = "moduleType is required")
    private FeedbackModuleType moduleType;

    private String referenceId;

    private FeedbackType feedbackType;

    @NotNull(message = "rating is required")
    @Min(value = 0, message = "rating minimum is 0")
    @Max(value = 10, message = "rating maximum is 10")
    private Integer rating;

    private String title;
    private String comment;
    private String categoryTags;
}
