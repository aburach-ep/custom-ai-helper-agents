package com.ai.poc.agent.testing.itests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class EmployeeControllerITestByJetbrCopilot {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/employees";
    }

    @Test
    void shouldReturnEmptyListWhenNoEmployeesExist() {
        // Delete all employees first to ensure empty state
        ResponseEntity<List<Map<String, Object>>> initialResponse = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        if (initialResponse.getBody() != null && !initialResponse.getBody().isEmpty()) {
            for (Map<String, Object> employee : initialResponse.getBody()) {
                Object id = employee.get("id");
                if (id != null) {
                    restTemplate.delete(baseUrl + "/" + id);
                }
            }
        }

        // Verify empty list is returned
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void shouldReturnErrorWhenCreatingEmployeeWithoutRequiredFields() {
        // Create employee with missing required fields
        Map<String, Object> incompleteEmployee = new HashMap<>();
        incompleteEmployee.put("email", "incomplete@example.com");
        // name is missing

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, incompleteEmployee, Map.class);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldReturnNotFoundWhenFetchingNonExistentEmployee() {
        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/9999999", Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldFilterEmployeesByJobTitle() {
        // Create employees with different job titles
        createEmployee("Dev One", "dev1@example.com", "Software Developer");
        createEmployee("Dev Two", "dev2@example.com", "Software Developer");
        createEmployee("QA One", "qa1@example.com", "QA Engineer");

        // Filter by job title
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                baseUrl + "/search?jobTitle={jobTitle}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {},
                "Software Developer"
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(2);
        assertThat(response.getBody().stream()
                .allMatch(emp -> "Software Developer".equals(emp.get("jobTitle"))))
                .isTrue();
    }

    @Test
    void shouldUpdateMultipleEmployeeFields() {
        // Create an employee
        Map<String, Object> employee = createEmployee("Update Test", "update@example.com", "Junior Developer");
        Object id = employee.get("id");

        // Update multiple fields
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Updated Name");
        updates.put("email", "new.email@example.com");
        updates.put("jobTitle", "Senior Developer");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);

        ResponseEntity<Map> updateResponse = restTemplate.exchange(
                baseUrl + "/" + id,
                HttpMethod.PUT,
                entity,
                Map.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify update
        ResponseEntity<Map> getResponse = restTemplate.getForEntity(baseUrl + "/" + id, Map.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).containsEntry("name", "Updated Name");
        assertThat(getResponse.getBody()).containsEntry("email", "new.email@example.com");
        assertThat(getResponse.getBody()).containsEntry("jobTitle", "Senior Developer");
    }

    @Test
    void shouldVerifyEmployeeLifecycle() {
        // 1. Create
        Map<String, Object> newEmployee = new HashMap<>();
        newEmployee.put("name", "Lifecycle Test");
        newEmployee.put("email", "lifecycle@example.com");
        newEmployee.put("jobTitle", "Test Engineer");

        ResponseEntity<Map> createResponse = restTemplate.postForEntity(baseUrl, newEmployee, Map.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> created = createResponse.getBody();
        Object id = created.get("id");

        // 2. Read
        ResponseEntity<Map> getResponse = restTemplate.getForEntity(baseUrl + "/" + id, Map.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 3. Update
        Map<String, Object> comp = new HashMap<>();
        comp.put("baseSalary", 95000);
        comp.put("bonus", 10000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(comp, headers);

        ResponseEntity<Void> patchResponse = restTemplate.exchange(
                baseUrl + "/" + id + "/compensation",
                HttpMethod.PATCH,
                entity,
                Void.class
        );
        assertThat(patchResponse.getStatusCode()).isIn(HttpStatus.NO_CONTENT, HttpStatus.OK);

        // 4. Verify update
        getResponse = restTemplate.getForEntity(baseUrl + "/" + id, Map.class);
        Map<String, Object> updatedEmployee = getResponse.getBody();
        Map<String, Object> compensation = (Map<String, Object>) updatedEmployee.get("compensation");
        assertThat(compensation).containsEntry("baseSalary", 95000);
        assertThat(compensation).containsEntry("bonus", 10000);

        // 5. Delete
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertThat(deleteResponse.getStatusCode()).isIn(HttpStatus.NO_CONTENT, HttpStatus.OK);

        // 6. Verify deletion
        ResponseEntity<Map> verifyDeleteResponse = restTemplate.getForEntity(baseUrl + "/" + id, Map.class);
        assertThat(verifyDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> createEmployee(String name, String email, String jobTitle) {
        Map<String, Object> employee = new HashMap<>();
        employee.put("name", name);
        employee.put("email", email);
        employee.put("jobTitle", jobTitle);

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, employee, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }
}