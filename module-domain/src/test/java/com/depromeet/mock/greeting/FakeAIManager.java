package com.depromeet.mock.greeting;

import com.depromeet.greeting.port.out.AIPort;

public class FakeAIManager implements AIPort {
    private static final String GENERATED_RESPONSE = "Hello, World!";

    @Override
    public String getSummary(String inputText) {
        return GENERATED_RESPONSE;
    }

    @Override
    public String getChatCompletions() {
        return GENERATED_RESPONSE;
    }
}
