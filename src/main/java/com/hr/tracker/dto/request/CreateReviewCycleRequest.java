package com.hr.tracker.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateReviewCycleRequest {

    @NotBlank(message = "Cycle name is required (e.g. 'Q1 2025')")
    @Size(max = 100)
    private String name;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

}
