package com.depromeet.memory.domain.vo;

import com.depromeet.member.domain.vo.MemberIdAndNickname;

public record MemoryIdAndDiaryAndMember(Long id, String diary, MemberIdAndNickname member) {}
