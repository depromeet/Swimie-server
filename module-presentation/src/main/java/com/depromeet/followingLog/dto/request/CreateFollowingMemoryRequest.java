package com.depromeet.followingLog.dto.request;

import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;

public record CreateFollowingMemoryRequest(Member member, Memory memory) {}
