package com.depromeet.report.facade;

import com.depromeet.image.domain.Image;
import com.depromeet.image.port.in.ImageGetUseCase;
import com.depromeet.member.domain.vo.MemberIdAndNickname;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.memory.domain.vo.MemoryIdAndDiaryAndMember;
import com.depromeet.memory.port.in.usecase.GetMemoryUseCase;
import com.depromeet.report.dto.request.ReportRequest;
import com.depromeet.report.mapper.ReportMapper;
import com.depromeet.report.port.in.command.CreateReportCommand;
import com.depromeet.report.port.in.usecase.CreateReportUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportFacade {
    private final MemberUseCase memberUseCase;
    private final ImageGetUseCase imageGetUseCase;
    private final GetMemoryUseCase getMemoryUseCase;
    private final CreateReportUseCase createReportUseCase;

    @Transactional
    public void save(Long memberId, ReportRequest request) {
        MemberIdAndNickname member = memberUseCase.findIdAndNicknameById(memberId);
        MemoryIdAndDiaryAndMember reportMemory =
                getMemoryUseCase.findIdAndNicknameById(request.memoryId());
        List<Image> images = imageGetUseCase.findImagesByMemoryId(reportMemory.id());
        CreateReportCommand command =
                ReportMapper.toCommand(member, reportMemory, images, request.reasonCode());
        createReportUseCase.save(command);
    }
}
