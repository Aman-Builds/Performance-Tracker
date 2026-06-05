package com.hr.tracker.service;

import com.hr.tracker.dto.request.SubmitReviewRequest;
import com.hr.tracker.dto.response.ReviewResponse;
import com.hr.tracker.entity.Employee;
import com.hr.tracker.entity.PerformanceReview;
import com.hr.tracker.entity.ReviewCycle;
import com.hr.tracker.exception.DuplicateResourceException;
import com.hr.tracker.repository.PerformanceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeService employeeService;
    private final ReviewCycleService cycleService;

    public ReviewResponse submitReview(SubmitReviewRequest request) {

        // These throw 404 if not found — caught by GlobalExceptionHandler
        Employee employee = employeeService.findById(request.getEmployeeId());
        ReviewCycle cycle = cycleService.findById(request.getCycleId());

        // Application-level duplicate check = clean 409, not a cryptic 500
        reviewRepository.findByEmployeeIdAndCycleIdAndReviewerId(
            request.getEmployeeId(),
            request.getCycleId(),
            request.getReviewerId()
        ).ifPresent(r -> {
            throw new DuplicateResourceException(
                String.format("Reviewer '%s' has already submitted a review for employee %d in cycle %d",
                    request.getReviewerId(), request.getEmployeeId(), request.getCycleId())
            );
        });

        PerformanceReview review = PerformanceReview.builder()
            .employee(employee)
            .cycle(cycle)
            .rating(request.getRating())
            .reviewerId(request.getReviewerId())
            .reviewerNotes(request.getReviewerNotes())
            .build();

        PerformanceReview saved = reviewRepository.save(review);
        return toResponse(saved);
    }

    // Fetch employee reviews and related cycle data in a single query avoiding N+! problem
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsForEmployee(Long employeeId) {
        // Ensure employee exists
        employeeService.findById(employeeId);

        return reviewRepository.findByEmployeeIdWithCycle(employeeId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    private ReviewResponse toResponse(PerformanceReview r) {
        return ReviewResponse.builder()
            .id(r.getId())
            .rating(r.getRating())
            .reviewerId(r.getReviewerId())
            .reviewerNotes(r.getReviewerNotes())
            .submittedAt(r.getSubmittedAt())
            .cycleId(r.getCycle().getId())
            .cycleName(r.getCycle().getName())
            .cycleStart(r.getCycle().getStartDate())
            .cycleEnd(r.getCycle().getEndDate())
            .build();
    }
}
