package com.depromeet.withdrawal.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.withdrawal.dto.request.WithdrawalReasonCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "탈퇴 사유(Withdrawal Reason)")
public interface WithdrawalReasonApi {
    @Operation(summary = "탈퇴 사유 제출")
    ApiResponse<?> create(@RequestBody WithdrawalReasonCreateRequest request);
}
