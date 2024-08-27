package com.depromeet.withdrawal.facade;

import com.depromeet.withdrawal.dto.request.WithdrawalReasonCreateRequest;
import com.depromeet.withdrawal.mapper.WithdrawalReasonMapper;
import com.depromeet.withdrawal.port.in.command.CreateWithdrawalReasonCommand;
import com.depromeet.withdrawal.port.in.usecase.CreateWithdrawalReasonUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawalReasonFacade {
    private final CreateWithdrawalReasonUseCase createWithdrawalReasonUseCase;

    public void save(WithdrawalReasonCreateRequest request) {
        CreateWithdrawalReasonCommand createWithdrawalReasonCommand =
                WithdrawalReasonMapper.toCommand(request);
        createWithdrawalReasonUseCase.save(createWithdrawalReasonCommand);
    }
}
