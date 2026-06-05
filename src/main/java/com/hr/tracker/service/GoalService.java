package com.hr.tracker.service;

import com.hr.tracker.dto.request.CreateGoalRequest;
import com.hr.tracker.entity.Employee;
import com.hr.tracker.entity.Goal;
import com.hr.tracker.entity.Goal.GoalStatus;
import com.hr.tracker.entity.ReviewCycle;
import com.hr.tracker.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final EmployeeService employeeService;
    private final ReviewCycleService cycleService;

    public Goal createGoal(CreateGoalRequest request) {
        Employee employee = employeeService.findById(request.getEmployeeId());
        ReviewCycle cycle = cycleService.findById(request.getCycleId());

        Goal goal = new Goal();
        goal.setEmployee(employee);
        goal.setCycle(cycle);
        goal.setTitle(request.getTitle());
        goal.setStatus(request.getStatus() != null ? request.getStatus() : GoalStatus.PENDING);

        return goalRepository.save(goal);
    }
}
