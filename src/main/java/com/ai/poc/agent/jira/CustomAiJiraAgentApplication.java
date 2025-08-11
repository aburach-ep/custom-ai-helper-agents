package com.ai.poc.agent.jira;

import com.ai.poc.agent.jira.service.JiraTicketAnalysisService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties
public class CustomAiJiraAgentApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CustomAiJiraAgentApplication.class, args);

        reviewAndAnalyzeJiraTicketReadiness(applicationContext, "EPMCDMETST", "RAP application");
        applicationContext.close();
    }

    private static void reviewAndAnalyzeJiraTicketReadiness(ConfigurableApplicationContext applicationContext, String projectName, String jiraSearchText) {
        var jiraTicketAnalysisService = applicationContext.getBean(JiraTicketAnalysisService.class);
        jiraTicketAnalysisService.analyzeTickets(projectName, jiraSearchText);
    }
} 