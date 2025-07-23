package com.epam.customaijira.service;

import com.epam.customaijira.client.JiraClient;
import com.epam.customaijira.client.DialClient;
import com.epam.customaijira.model.JiraIssue;
import com.epam.customaijira.model.DialRequest;
import com.epam.customaijira.model.DialResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketSummaryService {

    private final JiraClient jiraClient;
    private final DialClient dialClient;

    @Value("${jira.project}")
    private String jiraProject;

    @Value("${jira.subject}")
    private String jiraSubject;

    @Value("${dial.foundation-model}")
    private String dialModel;

    public TicketSummaryService(JiraClient jiraClient, DialClient dialClient) {
        this.jiraClient = jiraClient;
        this.dialClient = dialClient;
    }

    public List<Map<String, String>> getTicketSummaries() {
        String jql = String.format("project = %s AND summary ~ \"%s\"", jiraProject, jiraSubject);
        JiraIssue.SearchResult result = jiraClient.searchIssues(jql);

        List<Map<String, String>> summaries = new ArrayList<>();
        for (JiraIssue issue : result.issues) {
            String prompt = String.format(
                "Summarize the following Jira ticket in 3-5 sentences:\nTitle: %s\nDescription: %s",
                issue.fields.summary, issue.fields.description
            );
            DialRequest dialRequest = new DialRequest();
            dialRequest.model = dialModel;
            dialRequest.messages = List.of(Map.of("role", "user", "content", prompt));
            DialResponse dialResponse = dialClient.summarize(dialRequest);

            String summary = dialResponse.choices.get(0).message.content;
            summaries.add(Map.of(
                "key", issue.key,
                "summary", summary
            ));
        }
        return summaries;
    }
} 