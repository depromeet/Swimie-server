package com.depromeet.ai.port.in.usecase;

import com.depromeet.ai.port.in.CreateInputTextCommand;

public interface AIUseCase {
    String summary(CreateInputTextCommand inputText);

    String getMonthReport(CreateInputTextCommand inputText);
}
