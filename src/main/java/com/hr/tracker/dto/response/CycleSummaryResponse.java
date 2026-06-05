package com.hr.tracker.dto.response;

import lombok.Builder;
import lombok.Data;


@Data
public class CycleSummaryResponse {
    private Long cycleId;
    private String cycleName;
    private Double averageRating;
    private Long totalReviews;
    private String topPerformerName;
    private Long topPerformerId;
    private Double topPerformerAvgRating;
    private Long completedGoals;
    private Long missedGoals;
    private Long pendingGoals;
}
