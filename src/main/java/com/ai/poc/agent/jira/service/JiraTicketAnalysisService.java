package com.ai.poc.agent.jira.service;

import com.ai.poc.agent.jira.client.DialClient;
import com.ai.poc.agent.jira.dto.JiraSearchResponse;
import com.ai.poc.agent.jira.dto.JiraSearchResponseIssue;
import com.ai.poc.agent.jira.dto.DialResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JiraTicketAnalysisService {

    private static final String DIAL_API_VERSION = "2024-12-01-preview";

    private final JiraSearchService jiraSearchService;
    private final DialRequestBuilder dialRequestBuilder;
    private final DialClient dialClient;

    @Value("${dial.api-key}")
    private String dialApiKey;

    @Value("${dial.foundation-model}")
    private String dialModel;

    public List<Map<String, String>> analyzeTickets(String projectName, String searchText) {
        JiraSearchResponse searchResponse = jiraSearchService.findTickets(projectName, searchText);

        List<Map<String, String>> summaries = new ArrayList<>();
        for (JiraSearchResponseIssue issue : searchResponse.getIssues()) {
            var analyzeTicketRequest = dialRequestBuilder.mapJiraResponseIssueToDialRequest(issue);
            DialResponseDto dialResponseDto = dialClient.callChatApi(dialApiKey, DIAL_API_VERSION, analyzeTicketRequest);

            for (DialResponseDto.Choice dialResponseChoice : dialResponseDto.choices) {
                System.out.println("<--- Original issue Summary: " + issue.fields.summary + " --->\n");
                System.out.println("<--- Original issue Description: " + issue.fields.description + " --->\n");
                System.out.println("<--- DIAL response Message: " + dialResponseChoice.message.content + " --->\n");
            }

        }
        return summaries;
    }
} 