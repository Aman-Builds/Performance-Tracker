package com.hr.tracker.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class EmployeeResponse {
    private Long id;
    private String name;
    private String department;
    private String role;
    private LocalDate joiningDate;
    private LocalDateTime createdAt;
}
