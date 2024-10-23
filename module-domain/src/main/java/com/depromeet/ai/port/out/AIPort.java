package com.depromeet.ai.port.out;

public interface AIPort {
    String getSummary(String inputText);

    String getChatCompletions(String text);
}
