package com.depromeet.followingLog.port.in.command;

import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;

public record CreateFollowingMemoryCommand(Member member, Memory memory) {}
