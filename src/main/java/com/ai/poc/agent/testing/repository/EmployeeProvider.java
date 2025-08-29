package com.ai.poc.agent.testing.repository;

import com.ai.poc.agent.testing.model.Employee;
import com.ai.poc.agent.testing.model.Compensation;
import lombok.Getter;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class EmployeeProvider {

    @Getter
    private List<Employee> employees = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    @PostConstruct
    public void init() {
        Employee johnDoe = Employee.builder()
            .id(generateId())
            .name("John Doe")
            .email("john.doe@example.com")
            .jobTitle("Software Engineer")
            .compensation(Compensation.builder()
                .baseSalary(BigDecimal.valueOf(100000))
                .bonus(BigDecimal.valueOf(10000))
                .stockOptions(BigDecimal.valueOf(500))
                .build()
            )
            .build();
        Employee janeSmith = Employee.builder()
            .id(generateId())
            .name("Jane Smith")
            .email("jane.smith@example.com")
            .jobTitle("Product Manager")
            .compensation(Compensation.builder()
                .baseSalary(BigDecimal.valueOf(120000))
                .bonus(BigDecimal.valueOf(15000))
                .stockOptions(BigDecimal.valueOf(800))
                .build()
            )
            .build();
        Employee mikeJohnson = Employee.builder()
            .id(generateId())
            .name("Mike Johnson")
            .email("mike.johnson@example.com")
            .jobTitle("CTO")
            .compensation(Compensation.builder()
                .baseSalary(BigDecimal.valueOf(150000))
                .bonus(BigDecimal.valueOf(20000))
                .stockOptions(BigDecimal.valueOf(1000))
                .build()
            )
            .build();
        employees.addAll(List.of(johnDoe, janeSmith, mikeJohnson));
    }
    
    public Long generateId() {
        return idCounter.getAndIncrement();
    }

}
