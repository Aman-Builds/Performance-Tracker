package com.hr.tracker.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SubmitReviewRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Cycle ID is required")
    private Long cycleId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Reviewer ID is required")
    @Size(max = 100)
    private String reviewerId;

    private String reviewerNotes;
}
