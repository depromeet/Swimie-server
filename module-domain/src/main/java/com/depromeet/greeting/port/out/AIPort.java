package com.depromeet.greeting.port.out;

public interface AIPort {
    String getSummary(String inputText);

    String getChatCompletions();
}
