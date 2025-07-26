package com.ai.poc.agent.jira.service;

import com.ai.poc.agent.jira.client.JiraClient;
import com.ai.poc.agent.jira.dto.JiraSearchRequestDto;
import com.ai.poc.agent.jira.dto.JiraSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JiraSearchService {

    public static final String JIRA_REQUEST_TEMPLATE = "project = \"%s\" AND text ~ \"%s\"";
    private static final int MAX_SEARCH_RESULTS = 5;

    private final JiraClient jiraClient;

    public JiraSearchResponse findTickets(String projectName, String searchText) {

        var jiraSearchRequest = createJiraSearchRequestUsingTemplate(projectName, searchText);
        JiraSearchResponse searchResponse = jiraClient.findItems(jiraSearchRequest);
        return searchResponse;
    }

    private JiraSearchRequestDto createJiraSearchRequestUsingTemplate(String projectName, String jiraSearchText) {
        var jql = String.format(JIRA_REQUEST_TEMPLATE, projectName, jiraSearchText);
        var jiraSearchRequest = JiraSearchRequestDto.builder()
                .jql(jql)
                .maxResults(MAX_SEARCH_RESULTS)
                .build();
        return jiraSearchRequest;
    }

}
