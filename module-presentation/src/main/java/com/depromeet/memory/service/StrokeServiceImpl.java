package com.depromeet.memory.service;

import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.StrokeCreateRequest;
import com.depromeet.memory.dto.request.StrokeUpdateRequest;
import com.depromeet.memory.repository.StrokeRepository;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrokeServiceImpl implements StrokeService {
    private final StrokeRepository strokeRepository;

    @Override
    public Stroke save(Stroke stroke) {
        return strokeRepository.save(stroke);
    }

    @Override
    public List<Stroke> saveAll(Memory memory, List<StrokeCreateRequest> strokes) {
        if (strokes.isEmpty()) return null;
        List<Stroke> result = new CopyOnWriteArrayList<>();
        strokes.forEach(
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
        return strokeRepository.findAllByMemoryId(memoryId);
    }

    @Override
    public List<Stroke> updateAll(Memory memory, List<StrokeUpdateRequest> strokes) {
        List<Stroke> beforeStrokes = memory.getStrokes();

        if ((beforeStrokes == null || beforeStrokes.isEmpty())
                && (strokes == null || strokes.isEmpty())) {
            return null;
        }

        if (beforeStrokes == null || beforeStrokes.isEmpty()) {
            List<Stroke> result = new CopyOnWriteArrayList<>();
            strokes.forEach(
                    stroke -> {
                        Stroke updateStroke =
                                Stroke.builder()
                                        .memory(memory)
                                        .name(stroke.name())
                                        .laps(stroke.laps())
                                        .meter(stroke.meter())
                                        .build();
                        result.add(updateStroke);
                        strokeRepository.save(updateStroke);
                    });
            return result;
        }

        if (strokes == null || strokes.isEmpty()) {
            beforeStrokes.forEach(stroke -> strokeRepository.deleteById(stroke.getId()));
            return null;
        }

        List<Long> originStrokeIds = beforeStrokes.stream().map(Stroke::getId).toList();
        List<Long> updateStrokeIds = strokes.stream().map(StrokeUpdateRequest::id).toList();
        List<Long> existStrokeIds =
                originStrokeIds.stream()
                        .filter(id -> updateStrokeIds.stream().anyMatch(Predicate.isEqual(id)))
                        .toList();
        beforeStrokes.forEach(
                stroke -> {
                    if (!existStrokeIds.contains(stroke.getId())) {
                        strokeRepository.deleteById(stroke.getId());
                    }
                });
        strokes.forEach(
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
                    strokeRepository.save(updateStroke);
                });
        return getAllByMemoryId(memory.getId());
    }
}
