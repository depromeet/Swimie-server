package com.depromeet.ai.api;

import com.depromeet.ai.dto.request.SummaryTextRequest;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "AI - Clova Studio")
public interface AIApi {

    @Hidden
    @Operation(summary = "Clova Studio Summary API를 활용한 요약")
    ApiResponse<String> getSummaryByClovaStudio(@RequestBody SummaryTextRequest request);

    @Hidden
    @Operation(summary = "Clova Studio Chat Completions API를 활용한 1달간 수영기록 요약")
    ApiResponse<String> getMonthReportByClovaStudio(
            @LoginMember Long memberId,
            @RequestParam("year") Integer year,
            @RequestParam("month") Short month)
            throws JsonProcessingException;
}
