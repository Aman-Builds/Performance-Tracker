package com.hr.tracker.service;

import com.hr.tracker.dto.request.CreateEmployeeRequest;
import com.hr.tracker.dto.response.EmployeeResponse;
import com.hr.tracker.dto.response.EmployeeWithRatingResponse;
import com.hr.tracker.entity.Employee;
import com.hr.tracker.exception.ResourceNotFoundException;
import com.hr.tracker.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setDepartment(request.getDepartment());
        employee.setRole(request.getRole());
        employee.setJoiningDate(request.getJoiningDate());

        Employee saved = employeeRepository.save(employee);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<EmployeeWithRatingResponse> filterEmployees(String department, Double minRating) {
        List<Object[]> rows = employeeRepository.findByDepartmentAndMinRating(
                department,
                minRating != null ? minRating : 0.0
        );

        List<EmployeeWithRatingResponse> result = new ArrayList<>();
        for (Object[] row : rows) {
            Employee e = (Employee) row[0];
            Double avg = (Double) row[1];

            EmployeeWithRatingResponse dto = new EmployeeWithRatingResponse();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setDepartment(e.getDepartment());
            dto.setRole(e.getRole());
            dto.setJoiningDate(e.getJoiningDate());
            dto.setAverageRating(avg);
            result.add(dto);
        }
        return result;
    }

    private EmployeeResponse toResponse(Employee e) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(e.getId());
        response.setName(e.getName());
        response.setDepartment(e.getDepartment());
        response.setRole(e.getRole());
        response.setJoiningDate(e.getJoiningDate());
        response.setCreatedAt(e.getCreatedAt());
        return response;
    }
}