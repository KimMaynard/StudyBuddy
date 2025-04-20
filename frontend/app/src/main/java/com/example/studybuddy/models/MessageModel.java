package com.example.studybuddy.models;

public class MessageModel {
    private final String content;
    private final boolean isUser;

    public MessageModel(String content, boolean isUser) {
        this.content = content;
        this.isUser = isUser;
    }

    public String getContent() {
        return content;
    }

    public boolean isUser() {
        return isUser;
    }
}
