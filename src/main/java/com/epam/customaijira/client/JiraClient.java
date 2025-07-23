package com.epam.customaijira.client;

import com.epam.customaijira.model.JiraIssue;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "jiraClient",
    url = "${jira.base-url}/rest/api/2",
    configuration = com.epam.customaijira.config.FeignConfig.class
)
public interface JiraClient {
    @GetMapping("/search")
    JiraIssue.SearchResult searchIssues(
        @RequestParam("jql") String jql
    );
} 