package com.depromeet.memory.service;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.port.in.command.CreateStrokeCommand;
import com.depromeet.memory.port.in.command.UpdateStrokeCommand;
import com.depromeet.memory.port.in.usecase.StrokeUseCase;
import com.depromeet.memory.port.out.persistence.StrokePersistencePort;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrokeService implements StrokeUseCase {
    private final StrokePersistencePort strokePersistencePort;

    @Override
    public Stroke save(Stroke stroke) {
        return strokePersistencePort.save(stroke);
    }

    @Override
    public List<Stroke> saveAll(Memory memory, List<CreateStrokeCommand> commands) {
        if (commands.isEmpty()) return null;
        List<Stroke> result = new CopyOnWriteArrayList<>();
        commands.forEach(
                stroke -> {
                    Stroke newStroke =
                            this.save(
                                    Stroke.builder()
                                            .memory(memory)
                                            .name(stroke.name())
                                            .laps(stroke.laps())
                                            .meter(stroke.meter())
                                            .build());
                    result.add(newStroke);
                });
        return result;
    }

    @Override
    public List<Stroke> getAllByMemoryId(Long memoryId) {
        return strokePersistencePort.findAllByMemoryId(memoryId);
    }

    @Override
    public List<Stroke> updateAll(Memory memory, List<UpdateStrokeCommand> commands) {
        List<Stroke> beforeStrokes = memory.getStrokes();

        if ((beforeStrokes == null || beforeStrokes.isEmpty())
                && (commands == null || commands.isEmpty())) {
            return null;
        }

        if (beforeStrokes == null || beforeStrokes.isEmpty()) {
            List<Stroke> result = new CopyOnWriteArrayList<>();
            commands.forEach(
                    stroke -> {
                        Stroke updateStroke =
                                Stroke.builder()
                                        .memory(memory)
                                        .name(stroke.name())
                                        .laps(stroke.laps())
                                        .meter(stroke.meter())
                                        .build();
                        result.add(updateStroke);
                        strokePersistencePort.save(updateStroke);
                    });
            return result;
        }

        if (commands == null || commands.isEmpty()) {
            beforeStrokes.forEach(stroke -> strokePersistencePort.deleteById(stroke.getId()));
            return null;
        }

        List<Long> originStrokeIds = beforeStrokes.stream().map(Stroke::getId).toList();
        List<Long> updateStrokeIds = commands.stream().map(UpdateStrokeCommand::id).toList();
        List<Long> existStrokeIds =
                originStrokeIds.stream()
                        .filter(id -> updateStrokeIds.stream().anyMatch(Predicate.isEqual(id)))
                        .toList();
        beforeStrokes.forEach(
                stroke -> {
                    if (!existStrokeIds.contains(stroke.getId())) {
                        strokePersistencePort.deleteById(stroke.getId());
                    }
                });
        commands.forEach(
                stroke -> {
                    Stroke updateStroke;
                    if (stroke.id() != null) {
                        updateStroke =
                                Stroke.builder()
                                        .id(stroke.id())
                                        .memory(memory)
                                        .name(stroke.name())
                                        .laps(stroke.laps())
                                        .meter(stroke.meter())
                                        .build();
                    } else {
                        updateStroke =
                                Stroke.builder()
                                        .memory(memory)
                                        .name(stroke.name())
                                        .laps(stroke.laps())
                                        .meter(stroke.meter())
                                        .build();
                    }
                    strokePersistencePort.save(updateStroke);
                });
        return getAllByMemoryId(memory.getId());
    }
}
