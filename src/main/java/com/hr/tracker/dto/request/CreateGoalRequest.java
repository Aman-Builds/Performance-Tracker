package com.hr.tracker.dto.request;

import com.hr.tracker.entity.Goal.GoalStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateGoalRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Cycle ID is required")
    private Long cycleId;

    @NotBlank(message = "Goal title is required")
    @Size(max = 255)
    private String title;

    // Status is optional on creation — defaults to PENDING
    private GoalStatus status;
}
