package com.ai.poc.agent.jira.dto;

import java.util.List;

public class DialResponseDto {
    public List<Choice> choices;

    public static class Choice {
        public Message message;
    }

    public static class Message {
        public String content;
    }
} 