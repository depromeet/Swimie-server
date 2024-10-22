package com.depromeet.dto.ai;

import java.util.List;

public record ChatCompletionsRequest(List<ChatCompletionsMessage> messages) {
    public static ChatCompletionsRequest of(String systemContent, String userContent) {
        ChatCompletionsMessage systemMessage = ChatCompletionsMessage.of("system", systemContent);
        ChatCompletionsMessage userMessage = ChatCompletionsMessage.of("user", userContent);
        List<ChatCompletionsMessage> messages = List.of(systemMessage, userMessage);

        return new ChatCompletionsRequest(messages);
    }
}
