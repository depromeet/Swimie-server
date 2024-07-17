package com.depromeet.memory.service;

import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.StrokeCreateRequest;
import com.depromeet.memory.repository.StrokeRepository;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
}
