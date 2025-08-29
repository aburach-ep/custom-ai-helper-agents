package com.ai.poc.agent.testing.service;

import com.ai.poc.agent.testing.model.Compensation;
import com.ai.poc.agent.testing.model.Employee;
import com.ai.poc.agent.testing.repository.EmployeeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    
    private final EmployeeProvider employeeProvider;
    
    /**
     * Get all employees
     * @return List of all employees
     */
    public List<Employee> getEmployees() {
        log.info("Fetching all employees");
        return employeeProvider.getEmployees();
    }
    
    /**
     * Get all employees (alias for getEmployees)
     * @return List of all employees
     */
    public List<Employee> addEmployee(Employee employee) {
        log.info("Adding new employee: {}", employee.getName());
        if (employee.getId() == null) {
            employee.setId(employeeProvider.generateId());

        }
        employeeProvider.getEmployees().add(employee);
        return employeeProvider.getEmployees();
    }
    
    /**
     * Find employee by ID
     * @param id Employee ID to search for
     * @return Optional containing the employee if found
     */
    public Optional<Employee> findEmployeeById(Long id) {
        log.info("Searching for employee with ID: {}", id);
        return employeeProvider.getEmployees().stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Find employee by exact name (case-insensitive)
     * @param name Employee name to search for
     * @return Optional containing the employee if found
     */
    public Optional<Employee> findEmployeeByName(String name) {
        log.info("Searching for employee with name: {}", name);
        return employeeProvider.getEmployees().stream()
                .filter(employee -> StringUtils.equalsIgnoreCase(employee.getName(), name))
                .findFirst();
    }
    
    /**
     * Update employee's  Compensation
     * @param employeeId ID of the employee to update
     * @param compensation new compensation (can be null)
     */
    public void updateCompensation(Long employeeId, @Nullable Compensation compensation) {
        findEmployeeById(employeeId)
                .ifPresent(employee -> {
                    if (compensation != null) {
                        employee.setCompensation(compensation);
                        log.info("Updated compensation for employeeId = {}", employeeId);
                    }
                });
    }
    /**
     * Update employee's jobTitle
     * @param employeeId ID of the employee to update
     * @param jobTitle New job title
     */
    public void updateJobTitle(Long employeeId, String jobTitle) {
        log.info("Updating employee with ID: {}, jobTitle: {}", employeeId, jobTitle);

        findEmployeeById(employeeId)
                .ifPresent(employee -> {
                    employee.setJobTitle(jobTitle);
                    log.info("Updated jobTitle for employee: {}", employeeId);
                });
    }

    /**
     * Delete employee by ID
     * @param id Employee ID to delete
     */
    public void deleteById(Long id) {
        log.info("Deleting employee with ID: {}", id);
        // Note: This would need to be implemented in EmployeeProvider if you want to actually remove employees
        log.warn("Delete operation is not YET implemented in EmployeeProvider");
    }
}
