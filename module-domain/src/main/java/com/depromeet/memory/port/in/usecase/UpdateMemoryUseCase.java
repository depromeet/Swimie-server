package com.depromeet.memory.port.in.usecase;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.port.in.command.UpdateMemoryCommand;
import java.util.List;

public interface UpdateMemoryUseCase {
    Memory update(Long memoryId, UpdateMemoryCommand command, List<Stroke> strokes);
}
