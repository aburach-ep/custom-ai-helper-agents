package com.epam.customaijira.model;

import java.util.List;

public class JiraIssue {
    public String key;
    public Fields fields;

    public static class Fields {
        public String summary;
        public String description;
    }

    public static class SearchResult {
        public List<JiraIssue> issues;
    }
} 