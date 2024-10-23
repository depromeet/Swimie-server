package com.depromeet.ai.service;

import com.depromeet.ai.port.in.CreateInputTextCommand;
import com.depromeet.ai.port.in.usecase.AIUseCase;
import com.depromeet.ai.port.out.AIPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService implements AIUseCase {
    private final AIPort aiPort;

    @Override
    public String summary(CreateInputTextCommand inputText) {
        return aiPort.getSummary(inputText.text());
    }

    @Override
    public String getMonthReport(CreateInputTextCommand inputText) {
        return aiPort.getChatCompletions(inputText.text());
    }
}
