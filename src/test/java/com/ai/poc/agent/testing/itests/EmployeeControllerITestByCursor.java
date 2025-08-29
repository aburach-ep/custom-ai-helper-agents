package com.ai.poc.agent.testing.itests;

import com.ai.poc.agent.testing.model.Compensation;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class EmployeeControllerITestByCursor {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/employees";
        // Enable PATCH support, as default RestTemplate (and its default SimpleClientHttpRequestFactory)
        // does not support PATCH method when using JDK's HttpURLConnection
        CloseableHttpClient httpClient = HttpClients.createDefault();
        restTemplate.getRestTemplate().setRequestFactory(
                new HttpComponentsClientHttpRequestFactory(httpClient)
        );
    }

    @Test
    void shouldListEmployees() {
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldAddAndFetchEmployeeByIdAndName() {
        Map<String, Object> employee = new HashMap<>();
        employee.put("name", "Alice Johnson");
        employee.put("email", "alice.johnson@example.com");
        employee.put("jobTitle", "QA Engineer");
        Map<String, Object> comp = new HashMap<>();
        comp.put("baseSalary", 90000);
        comp.put("bonus", 8000);
        comp.put("stockOptions", 300);
        employee.put("compensation", comp);

        ResponseEntity<Map> createResp = restTemplate.postForEntity(baseUrl, employee, Map.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map created = createResp.getBody();
        assertThat(created).isNotNull();

        // Try to read back by name
        ResponseEntity<Map> byName = restTemplate.getForEntity(baseUrl + "/search?name={name}", Map.class, "Alice Johnson");
        assertThat(byName.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(byName.getBody()).isNotNull();

        // If an id is present, fetch by id too
        Object id = created.get("id");
        if (id != null) {
            ResponseEntity<Map> byId = restTemplate.getForEntity(baseUrl + "/" + id, Map.class);
            assertThat(byId.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(byId.getBody()).isNotNull();
        }
    }

    @Test
    void shouldUpdateJobTitleAndCompensation() {
        // Create an employee first
        Map<String, Object> employee = new HashMap<>();
        employee.put("name", "Bob QA");
        employee.put("email", "bob.qa@example.com");
        employee.put("jobTitle", "QA Engineer");
        ResponseEntity<Map> createResp = restTemplate.postForEntity(baseUrl, employee, Map.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map created = createResp.getBody();
        assertThat(created).isNotNull();
        Object id = created.get("id");

        // Update job title
        String newTitle = "Senior QA Engineer";
        ResponseEntity<Void> updTitle = restTemplate.exchange(
                baseUrl + "/" + id + "/job-title?jobTitle=" + newTitle,
                HttpMethod.PATCH,
                null,
                Void.class
        );
        assertThat(updTitle.getStatusCode()).isIn(HttpStatus.NO_CONTENT, HttpStatus.OK);

        // Update compensation
        var newCompensation = Compensation.builder()
                .baseSalary(BigDecimal.valueOf(110000L))
                .bonus(BigDecimal.valueOf(12000L))
                .stockOptions(BigDecimal.valueOf(300L))
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Compensation> entity = new HttpEntity<>(newCompensation, headers);
        ResponseEntity<Void> updComp = restTemplate.exchange(
                baseUrl + "/" + id + "/compensation",
                HttpMethod.PATCH,
                entity,
                Void.class
        );
        assertThat(updComp.getStatusCode()).isIn(HttpStatus.NO_CONTENT, HttpStatus.OK);
    }

    @Test
    void shouldDeleteEmployee() {
        // Create an employee to delete
        Map<String, Object> employee = new HashMap<>();
        employee.put("name", "To Delete");
        employee.put("email", "delete.me@example.com");
        employee.put("jobTitle", "Temp");
        ResponseEntity<Map> createResp = restTemplate.postForEntity(baseUrl, employee, Map.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map created = createResp.getBody();
        assertThat(created).isNotNull();
        Object id = created.get("id");

        ResponseEntity<Void> delResp = restTemplate.exchange(
                baseUrl + "/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertThat(delResp.getStatusCode()).isIn(HttpStatus.NO_CONTENT, HttpStatus.OK);
    }
}
