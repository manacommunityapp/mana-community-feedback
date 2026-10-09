package com.manacommunity.feedback.dto;

import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackAnalyticsSummary {
    private String communityId;
    private FeedbackModuleType moduleType;
    private long totalSubmissions;
    private double averageRating;
    private double csatPercentage;
    private double npsScore;
    private long positiveSentimentCount;
    private long neutralSentimentCount;
    private long negativeSentimentCount;
    private Map<FeedbackModuleType, Long> moduleBreakdown;
}
