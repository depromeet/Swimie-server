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
        // 기존 영법 Id 목록
        List<Long> originStrokeIds = beforeStrokes.stream().map(Stroke::getId).toList();
        // 수정된 영법 Id 목록
        List<Long> updateStrokeIds = strokes.stream().map(StrokeUpdateRequest::id).toList();
        // 중복되는 Id 목록
        List<Long> existStrokeIds =
                originStrokeIds.stream()
                        .filter(id -> updateStrokeIds.stream().anyMatch(Predicate.isEqual(id)))
                        .toList();
        beforeStrokes.forEach(
                stroke -> {
                    if (!existStrokeIds.contains(stroke.getId())) {
                        // 수정된 영법에 남아있지 않으면 지운다
                        strokeRepository.deleteById(stroke.getId());
                    }
                });
        strokes.forEach(
                stoke -> {
                    if (stoke.id() != null) {
                        // 수정된 영법이면 업데이트한다
                        Stroke updateStroke =
                                Stroke.builder()
                                        .id(stoke.id())
                                        .memory(memory)
                                        .name(stoke.name())
                                        .laps(stoke.laps())
                                        .meter(stoke.meter())
                                        .build();
                        strokeRepository.save(updateStroke);
                    } else {
                        // 새로운 영법이면 저장한다
                        Stroke updateStroke =
                                Stroke.builder()
                                        .memory(memory)
                                        .name(stoke.name())
                                        .laps(stoke.laps())
                                        .meter(stoke.meter())
                                        .build();
                        strokeRepository.save(updateStroke);
                    }
                });
        return getAllByMemoryId(memory.getId());
    }
}
