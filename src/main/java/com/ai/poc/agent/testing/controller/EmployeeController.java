package com.ai.poc.agent.testing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import com.ai.poc.agent.testing.model.Compensation;
import com.ai.poc.agent.testing.model.Employee;
import com.ai.poc.agent.testing.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
	
	private final EmployeeService employeeService;
	
	// List all employees
	@GetMapping
	public ResponseEntity<List<Employee>> getEmployees() {
		return ResponseEntity.ok(employeeService.getEmployees());
	}
	
	// Add a new employee
	@PostMapping
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
		employeeService.addEmployee(employee);
		return ResponseEntity.ok(employee);
	}
	
	// Find employee by ID
	@GetMapping("/{id}")
	public ResponseEntity<Employee> findEmployeeById(@PathVariable Long id) {
		Optional<Employee> employee = employeeService.findEmployeeById(id);
		return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	// Find employee by exact name (case-insensitive)
	@GetMapping("/search")
	public ResponseEntity<Employee> findEmployeeByName(@RequestParam String name) {
		Optional<Employee> employee = employeeService.findEmployeeByName(name);
		return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	// Update employee compensation (nullable body allowed)
	@PatchMapping("/{id}/compensation")
	public ResponseEntity<Void> updateCompensation(
			@PathVariable Long id,
			@RequestBody(required = false) @Nullable Compensation compensation) {
		employeeService.updateCompensation(id, compensation);
		return ResponseEntity.noContent().build();
	}
	
	// Update employee job title
	@PatchMapping("/{id}/job-title")
	public ResponseEntity<Void> updateJobTitle(
			@PathVariable Long id,
			@RequestParam String jobTitle) {
		employeeService.updateJobTitle(id, jobTitle);
		return ResponseEntity.noContent().build();
	}
	
	// Delete employee by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		employeeService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
