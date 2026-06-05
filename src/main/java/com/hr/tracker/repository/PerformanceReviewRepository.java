package com.hr.tracker.repository;

import com.hr.tracker.entity.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {

    // Fetch cycle along with reviews in a single query to avoid the N+1 problem.
    @Query("""
        SELECT r FROM PerformanceReview r
        JOIN FETCH r.cycle
        WHERE r.employee.id = :employeeId
        ORDER BY r.submittedAt DESC
        """)
    List<PerformanceReview> findByEmployeeIdWithCycle(@Param("employeeId") Long employeeId);

    // Validating uniqueness before saving to avoid database constraint errors.
    Optional<PerformanceReview> findByEmployeeIdAndCycleIdAndReviewerId(
        Long employeeId, Long cycleId, String reviewerId
    );

    // Retrieves average rating and review count for a cycle.
    @Query("""
        SELECT AVG(r.rating), COUNT(r)
        FROM PerformanceReview r
        WHERE r.cycle.id = :cycleId
        """)
    List<Object[]> getCycleAggregates(@Param("cycleId") Long cycleId);

    // Returns employees ordered by average rating, with the top performer first.
    @Query("""
        SELECT r.employee.id, r.employee.name, AVG(r.rating) as avgRating
        FROM PerformanceReview r
        WHERE r.cycle.id = :cycleId
        GROUP BY r.employee.id, r.employee.name
        ORDER BY avgRating DESC
        """)
    List<Object[]> findTopPerformerByCycleId(@Param("cycleId") Long cycleId);
}
