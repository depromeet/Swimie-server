package com.depromeet.report.port.in.command;

import com.depromeet.image.domain.Image;
import com.depromeet.member.domain.vo.MemberIdAndNickname;
import com.depromeet.memory.domain.vo.MemoryIdAndDiaryAndMember;
import com.depromeet.report.domain.ReportReasonCode;
import java.util.List;

public record CreateReportCommand(
        MemberIdAndNickname member,
        MemoryIdAndDiaryAndMember reportMemory,
        List<Image> images,
        ReportReasonCode reasonCode) {}
