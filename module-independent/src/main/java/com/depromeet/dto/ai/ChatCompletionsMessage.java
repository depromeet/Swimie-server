package com.depromeet.dto.ai;

public record ChatCompletionsMessage(String role, String content) {
    public static ChatCompletionsMessage of(String role, String content) {
        return new ChatCompletionsMessage(role, content);
    }
}
