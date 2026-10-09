package com.manacommunity.feedback.service;

import com.manacommunity.feedback.dto.FeedbackAnalyticsSummary;
import com.manacommunity.feedback.model.FeedbackSubmissionEntity;
import com.manacommunity.feedback.model.enums.FeedbackModuleType;
import com.manacommunity.feedback.model.enums.FeedbackType;
import com.manacommunity.feedback.model.enums.SentimentType;
import com.manacommunity.feedback.repository.FeedbackSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackAnalyticsService {

    private final FeedbackSubmissionRepository feedbackRepository;

    @Transactional(readOnly = true)
    public FeedbackAnalyticsSummary getAnalytics(String communityId, FeedbackModuleType moduleType) {
        List<FeedbackSubmissionEntity> list = (moduleType != null)
                ? feedbackRepository.findByCommunityIdAndModuleType(communityId, moduleType)
                : feedbackRepository.findByCommunityId(communityId);

        if (list.isEmpty()) {
            return FeedbackAnalyticsSummary.builder()
                    .communityId(communityId)
                    .moduleType(moduleType)
                    .totalSubmissions(0)
                    .averageRating(0.0)
                    .csatPercentage(0.0)
                    .npsScore(0.0)
                    .positiveSentimentCount(0)
                    .neutralSentimentCount(0)
                    .negativeSentimentCount(0)
                    .moduleBreakdown(Map.of())
                    .build();
        }

        long total = list.size();
        double sumRating = 0.0;
        long csatSatisfied = 0;
        long promoters = 0;
        long detractors = 0;
        long positiveCount = 0;
        long neutralCount = 0;
        long negativeCount = 0;

        Map<FeedbackModuleType, Long> moduleMap = new EnumMap<>(FeedbackModuleType.class);

        for (FeedbackSubmissionEntity f : list) {
            sumRating += f.getRating();
            moduleMap.merge(f.getModuleType(), 1L, Long::sum);

            // CSAT count (rating 4 or 5 out of 5, or 8+ out of 10)
            if (f.getFeedbackType() == FeedbackType.NPS_0_10) {
                if (f.getRating() >= 8) csatSatisfied++;
                if (f.getRating() >= 9) promoters++;
                else if (f.getRating() <= 6) detractors++;
            } else {
                if (f.getRating() >= 4) {
                    csatSatisfied++;
                    promoters++;
                } else if (f.getRating() <= 2) {
                    detractors++;
                }
            }

            if (f.getSentiment() == SentimentType.POSITIVE) positiveCount++;
            else if (f.getSentiment() == SentimentType.NEGATIVE) negativeCount++;
            else neutralCount++;
        }

        double avgRating = round(sumRating / total);
        double csatPct = round(((double) csatSatisfied / total) * 100.0);
        double nps = round((((double) promoters - detractors) / total) * 100.0);

        return FeedbackAnalyticsSummary.builder()
                .communityId(communityId)
                .moduleType(moduleType)
                .totalSubmissions(total)
                .averageRating(avgRating)
                .csatPercentage(csatPct)
                .npsScore(nps)
                .positiveSentimentCount(positiveCount)
                .neutralSentimentCount(neutralCount)
                .negativeSentimentCount(negativeCount)
                .moduleBreakdown(moduleMap)
                .build();
    }

    private double round(double val) {
        return BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
