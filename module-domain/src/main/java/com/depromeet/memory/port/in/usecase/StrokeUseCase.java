package com.depromeet.memory.port.in.usecase;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.port.in.command.CreateStrokeCommand;
import com.depromeet.memory.port.in.command.UpdateStrokeCommand;
import java.util.List;

public interface StrokeUseCase {
    Stroke save(Stroke stroke);

    List<Stroke> saveAll(Memory memory, List<CreateStrokeCommand> commands);

    List<Stroke> getAllByMemoryId(Long memoryId);

    List<Stroke> updateAll(Memory memory, List<UpdateStrokeCommand> commands);

    void deleteAllByMemoryId(List<Long> memoryIds);
}
