package com.manacommunity.feedback.dto;

import com.manacommunity.feedback.model.enums.FeedbackStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateFeedbackStatusRequest {

    @NotNull(message = "status is required")
    private FeedbackStatus status;

    private String resolutionNotes;
}
