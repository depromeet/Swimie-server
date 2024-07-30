package com.depromeet.memory.port.in.usecase;

import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.port.in.command.CreateMemoryCommand;

public interface CreateMemoryUseCase {
    Memory save(Member member, CreateMemoryCommand command);
}
