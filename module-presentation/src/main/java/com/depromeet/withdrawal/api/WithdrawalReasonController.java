package com.depromeet.withdrawal.api;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.type.withdrawal.WithdrawalReasonSuccessType;
import com.depromeet.withdrawal.dto.request.WithdrawalReasonCreateRequest;
import com.depromeet.withdrawal.facade.WithdrawalReasonFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/withdrawal")
public class WithdrawalReasonController implements WithdrawalReasonApi {
    private final WithdrawalReasonFacade withdrawalReasonFacade;

    @PostMapping
    @Logging(item = "WithdrawalReason", action = "POST")
    public ApiResponse<?> create(@RequestBody WithdrawalReasonCreateRequest request) {
        withdrawalReasonFacade.save(request);
        return ApiResponse.success(WithdrawalReasonSuccessType.POST_WITHDRAWAL_REASON_SUCCESS);
    }
}
