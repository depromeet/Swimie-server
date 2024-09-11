package com.depromeet.report.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.report.dto.request.ReportRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "기록 신고(Report)")
public interface ReportApi {
    @Operation(summary = "기록 신고 제출")
    ApiResponse<?> create(@LoginMember Long memberId, @RequestBody ReportRequest request);
}
