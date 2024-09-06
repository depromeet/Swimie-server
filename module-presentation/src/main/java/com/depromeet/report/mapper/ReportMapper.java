package com.depromeet.report.mapper;

import com.depromeet.image.domain.Image;
import com.depromeet.member.domain.vo.MemberIdAndNickname;
import com.depromeet.memory.domain.vo.MemoryIdAndDiaryAndMember;
import com.depromeet.report.domain.ReportReasonCode;
import com.depromeet.report.port.in.command.CreateReportCommand;
import java.util.List;

public class ReportMapper {
    public static CreateReportCommand toCommand(
            MemberIdAndNickname member,
            MemoryIdAndDiaryAndMember reportMemory,
            List<Image> images,
            ReportReasonCode reasonCode) {
        return new CreateReportCommand(member, reportMemory, images, reasonCode);
    }
}
