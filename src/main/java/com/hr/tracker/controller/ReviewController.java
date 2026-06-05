package com.hr.tracker.controller;

import com.hr.tracker.dto.request.SubmitReviewRequest;
import com.hr.tracker.dto.response.ReviewResponse;
import com.hr.tracker.service.PerformanceReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final PerformanceReviewService reviewService;

    /**
     * POST /reviews
     * Submits a performance review. Returns 201 on success, 409 if duplicate.
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> submitReview(
            @Valid @RequestBody SubmitReviewRequest request) {

        ReviewResponse response = reviewService.submitReview(request);
        return ResponseEntity.status(201).body(response);
    }
}
