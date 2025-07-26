package com.ai.poc.agent.jira.controller;

import com.ai.poc.agent.jira.service.JiraTicketAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TicketController {

    private final JiraTicketAnalysisService jiraTicketAnalysisService;

    public TicketController(JiraTicketAnalysisService jiraTicketAnalysisService) {
        this.jiraTicketAnalysisService = jiraTicketAnalysisService;
    }

    @GetMapping("/tickets/summaries")
    public List<Map<String, String>> getTicketSummaries() {
        return jiraTicketAnalysisService.analyzeTickets("EPMCDMETST", "ai agent");
    }
} 