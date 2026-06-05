package com.hr.tracker.controller;

import com.hr.tracker.dto.request.CreateReviewCycleRequest;
import com.hr.tracker.dto.response.CycleSummaryResponse;
import com.hr.tracker.entity.ReviewCycle;
import com.hr.tracker.service.ReviewCycleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cycles")
@RequiredArgsConstructor
public class CycleController {

    private final ReviewCycleService cycleService;

    @PostMapping
    public ResponseEntity<ReviewCycle> createCycle(@Valid @RequestBody CreateReviewCycleRequest request) {
        ReviewCycle saved = cycleService.createCycle(request);
        return ResponseEntity.status(201).body(saved);
    }

    /**
     * GET /cycles/{id}/summary
     * Returns aggregated stats for a review cycle.
     */
    @GetMapping("/{id}/summary")
    public ResponseEntity<CycleSummaryResponse> getCycleSummary(@PathVariable Long id) {
        return ResponseEntity.ok(cycleService.getCycleSummary(id));
    }
}
