package com.hr.tracker.repository;

import com.hr.tracker.entity.Goal;
import com.hr.tracker.entity.Goal.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {


    @Query("""
        SELECT g.status, COUNT(g)
        FROM Goal g
        WHERE g.cycle.id = :cycleId
        GROUP BY g.status
        """)
    List<Object[]> countByStatusForCycle(@Param("cycleId") Long cycleId);

    long countByCycleIdAndStatus(Long cycleId, GoalStatus status);
}
