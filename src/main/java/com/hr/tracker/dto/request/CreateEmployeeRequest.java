package com.hr.tracker.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateEmployeeRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be under 255 characters")
    private String name;

    @NotBlank(message = "Department is required")
    @Size(max = 100)
    private String department;

    @NotBlank(message = "Role is required")
    @Size(max = 100)
    private String role;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;
}
