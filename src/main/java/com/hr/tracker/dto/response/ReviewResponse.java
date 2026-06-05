package com.hr.tracker.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String reviewerId;
    private String reviewerNotes;
    private LocalDateTime submittedAt;
    private Long cycleId;
    private String cycleName;
    private LocalDate cycleStart;
    private LocalDate cycleEnd;
}
