package com.hr.tracker.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

/**
 * Used by GET /employees?department=&minRating=
 * Includes averageRating so the client can see why the employee was included.
 */
@Data
public class EmployeeWithRatingResponse {
    private Long id;
    private String name;
    private String department;
    private String role;
    private LocalDate joiningDate;
    private Double averageRating;
}
