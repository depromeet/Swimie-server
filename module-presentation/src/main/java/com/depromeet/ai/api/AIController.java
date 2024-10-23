package com.depromeet.ai.api;

import com.depromeet.ai.dto.request.SummaryTextRequest;
import com.depromeet.ai.facade.AIFacade;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.type.ai.AISuccessType;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController implements AIApi {
    private final AIFacade aiFacade;

    @PostMapping
    public ApiResponse<String> getSummaryByClovaStudio(@RequestBody SummaryTextRequest request) {
        String response = aiFacade.summary(request);

        return ApiResponse.success(AISuccessType.GET_RESPONSE_SUCCESS, response);
    }

    @GetMapping
    public ApiResponse<String> getMonthReportByClovaStudio(
            @LoginMember Long memberId,
            @RequestParam("year") Integer year,
            @RequestParam("month") Short month)
            throws JsonProcessingException {
        String response = aiFacade.getMonthReport(memberId, year, month);
        return ApiResponse.success(AISuccessType.GET_RESPONSE_SUCCESS, response);
    }
}
