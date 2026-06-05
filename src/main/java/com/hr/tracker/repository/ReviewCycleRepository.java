package com.hr.tracker.repository;

import com.hr.tracker.entity.ReviewCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReviewCycleRepository extends JpaRepository<ReviewCycle, Long> {

    // Used to check for duplicate cycle names before saving
    Optional<ReviewCycle> findByNameIgnoreCase(String name);
}
