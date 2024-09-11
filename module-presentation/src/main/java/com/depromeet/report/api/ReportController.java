package com.depromeet.report.api;

import com.depromeet.config.log.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.report.dto.request.ReportRequest;
import com.depromeet.report.facade.ReportFacade;
import com.depromeet.type.report.ReportSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController implements ReportApi {
    private final ReportFacade reportFacade;

    @PostMapping
    @Logging(item = "Report", action = "POST")
    public ApiResponse<?> create(
            @LoginMember Long memberId, @Valid @RequestBody ReportRequest request) {
        reportFacade.save(memberId, request);
        return ApiResponse.success(ReportSuccessType.POST_RESULT_SUCCESS);
    }
}
