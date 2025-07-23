package com.epam.customaijira.model;

import java.util.List;

public class DialResponse {
    public List<Choice> choices;

    public static class Choice {
        public Message message;
    }

    public static class Message {
        public String content;
    }
} 