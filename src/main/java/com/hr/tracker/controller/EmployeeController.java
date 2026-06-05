package com.hr.tracker.controller;

import com.hr.tracker.dto.request.CreateEmployeeRequest;
import com.hr.tracker.dto.response.EmployeeResponse;
import com.hr.tracker.dto.response.EmployeeWithRatingResponse;
import com.hr.tracker.dto.response.ReviewResponse;
import com.hr.tracker.service.EmployeeService;
import com.hr.tracker.service.PerformanceReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PerformanceReviewService reviewService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(201).body(response);
    }


    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewsForEmployee(id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeWithRatingResponse>> filterEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double minRating) {

        return ResponseEntity.ok(employeeService.filterEmployees(department, minRating));
    }
}
