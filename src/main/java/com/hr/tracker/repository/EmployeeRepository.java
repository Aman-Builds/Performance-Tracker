package com.hr.tracker.repository;

import com.hr.tracker.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
    SELECT e, AVG(r.rating)
    FROM Employee e
    JOIN e.reviews r
    WHERE (:department IS NULL OR LOWER(e.department) = LOWER(:department))
    GROUP BY e
    HAVING AVG(r.rating) >= :minRating
    ORDER BY AVG(r.rating) DESC
    """)
    List<Object[]> findByDepartmentAndMinRating(
            @Param("department") String department,
            @Param("minRating") Double minRating
    );
}
