package com.hr.tracker.service;

import com.hr.tracker.dto.request.CreateReviewCycleRequest;
import com.hr.tracker.dto.response.CycleSummaryResponse;
import com.hr.tracker.entity.Goal.GoalStatus;
import com.hr.tracker.entity.ReviewCycle;
import com.hr.tracker.exception.DuplicateResourceException;
import com.hr.tracker.exception.ResourceNotFoundException;
import com.hr.tracker.repository.GoalRepository;
import com.hr.tracker.repository.PerformanceReviewRepository;
import com.hr.tracker.repository.ReviewCycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCycleService {

    private final ReviewCycleRepository cycleRepository;
    private final PerformanceReviewRepository reviewRepository;
    private final GoalRepository goalRepository;

    public ReviewCycle createCycle(CreateReviewCycleRequest request) {
        if (!request.getEndDate().isAfter(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        cycleRepository.findByNameIgnoreCase(request.getName()).ifPresent(c -> {
            throw new DuplicateResourceException("Review cycle already exists: " + request.getName());
        });

        ReviewCycle cycle = new ReviewCycle();
        cycle.setName(request.getName());
        cycle.setStartDate(request.getStartDate());
        cycle.setEndDate(request.getEndDate());

        return cycleRepository.save(cycle);
    }

    @Transactional(readOnly = true)
    public ReviewCycle findById(Long id) {
        return cycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review cycle not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public CycleSummaryResponse getCycleSummary(Long cycleId) {

        ReviewCycle cycle = findById(cycleId);

        Double averageRating = 0.0;
        Long totalReviews = 0L;

        List<Object[]> aggregates = reviewRepository.getCycleAggregates(cycleId);
        if (!aggregates.isEmpty()) {
            Object[] result = aggregates.get(0);
            averageRating = result[0] == null ? 0.0 : ((Number) result[0]).doubleValue();
            totalReviews = result[1] == null ? 0L : ((Number) result[1]).longValue();
        }

        Long topPerformerId = null;
        String topPerformerName = null;
        Double topPerformerAvgRating = null;

        List<Object[]> topPerformer = reviewRepository.findTopPerformerByCycleId(cycleId);
        if (!topPerformer.isEmpty()) {
            Object[] result = topPerformer.get(0);
            topPerformerId = ((Number) result[0]).longValue();
            topPerformerName = (String) result[1];
            topPerformerAvgRating = ((Number) result[2]).doubleValue();
        }

        long completedGoals = 0;
        long pendingGoals = 0;
        long missedGoals = 0;

        for (Object[] result : goalRepository.countByStatusForCycle(cycleId)) {
            GoalStatus status = (GoalStatus) result[0];
            long count = ((Number) result[1]).longValue();

            switch (status) {
                case COMPLETED -> completedGoals = count;
                case PENDING -> pendingGoals = count;
                case MISSED -> missedGoals = count;
            }
        }

        CycleSummaryResponse response = new CycleSummaryResponse();
        response.setCycleId(cycle.getId());
        response.setCycleName(cycle.getName());
        response.setAverageRating(averageRating);
        response.setTotalReviews(totalReviews);
        response.setTopPerformerId(topPerformerId);
        response.setTopPerformerName(topPerformerName);
        response.setTopPerformerAvgRating(topPerformerAvgRating);
        response.setCompletedGoals(completedGoals);
        response.setPendingGoals(pendingGoals);
        response.setMissedGoals(missedGoals);

        return response;
    }
}