package com.depromeet.ai.facade;

import com.depromeet.ai.dto.request.SummaryTextRequest;
import com.depromeet.ai.port.in.CreateInputTextCommand;
import com.depromeet.ai.port.in.usecase.AIUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.dto.response.CalendarResponse;
import com.depromeet.memory.port.in.usecase.CalendarUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AIFacade {
    private final AIUseCase aiUseCase;
    private final MemberUseCase memberUseCase;
    private final CalendarUseCase calendarUseCase;
    private ObjectMapper objectMapper = new ObjectMapper();

    public String summary(SummaryTextRequest request) {
        CreateInputTextCommand inputTextCommand = new CreateInputTextCommand(request.text());

        return aiUseCase.summary(inputTextCommand);
    }

    public String getMonthReport(Long memberId, Integer year, Short month)
            throws JsonProcessingException {
        YearMonth yearMonth = YearMonth.of(year, month);
        Member member = memberUseCase.findById(memberId);
        List<Memory> calendarMemories =
                calendarUseCase.getCalendarByYearAndMonth(memberId, yearMonth);
        CalendarResponse response = CalendarResponse.of(member, calendarMemories);
        String inputText = objectMapper.writeValueAsString(response);

        CreateInputTextCommand inputTextCommand = new CreateInputTextCommand(inputText);
        return aiUseCase.getMonthReport(inputTextCommand);
    }
}
