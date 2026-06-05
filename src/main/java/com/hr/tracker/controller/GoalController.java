package com.hr.tracker.controller;

import com.hr.tracker.dto.request.CreateGoalRequest;
import com.hr.tracker.entity.Goal;
import com.hr.tracker.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;


    @PostMapping
    public ResponseEntity<Goal> createGoal(
            @Valid @RequestBody CreateGoalRequest request) {

        Goal saved = goalService.createGoal(request);
        return ResponseEntity.status(201).body(saved);
    }
}
