package com.depromeet.dto.ai;

import java.util.List;

public record ChatCompletionsRequest(
        List<ChatCompletionsMessage> messages,
        double topP,
        int topK,
        int maxTokens,
        double temperature,
        double repeatPenalty,
        boolean includeAiFilters,
        int seed) {
    public static ChatCompletionsRequest of(String systemContent) {
        var systemMessage = ChatCompletionsMessage.of("system", systemContent);
        var assistantMessage1 = ChatCompletionsMessage.of("assistant", "오늘도 물살을 가르며, 건강한 하루 보내요!");
        var assistantMessage2 = ChatCompletionsMessage.of("assistant", "수영 기록이 벌써 많이 쌓였네! 대단해요!");
        var assistantMessage3 =
                ChatCompletionsMessage.of("assistant", "조금만 더 힘을 내봐요! 충분히 잘하고 있어요!");
        var assistantMessage4 = ChatCompletionsMessage.of("assistant", "꾸준함이 실력을 빛나게 해줄 거예요!");
        var assistantMessage5 =
                ChatCompletionsMessage.of("assistant", "편안하게 호흡하며, 자유로운 헤엄 즐겨 보아요!");
        var messages =
                List.of(
                        systemMessage,
                        assistantMessage1,
                        assistantMessage2,
                        assistantMessage3,
                        assistantMessage4,
                        assistantMessage5);

        return new ChatCompletionsRequest(messages, 0.8, 0, 100, 0.5, 5.0, true, 0);
    }
}
