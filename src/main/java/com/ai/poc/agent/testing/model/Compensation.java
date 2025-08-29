package com.ai.poc.agent.testing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compensation {
    
    private BigDecimal baseSalary;
    private BigDecimal bonus;
    private BigDecimal stockOptions;
}
