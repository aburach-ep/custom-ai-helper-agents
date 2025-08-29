package com.ai.poc.agent.testing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    private Long id;
    
    private String name;
    
    private String email;
    
    private String jobTitle;
    
    private com.ai.poc.agent.testing.model.Compensation compensation;
    
}
