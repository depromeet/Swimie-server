package com.depromeet.memory.service;

import com.depromeet.exception.NotFoundException;
import com.depromeet.member.Member;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.repository.MemoryDetailRepository;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.pool.Pool;
import com.depromeet.pool.repository.PoolRepository;
import com.depromeet.type.memory.MemoryErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemoryServiceImpl implements MemoryService {
    private final PoolRepository poolRepository;
    private final MemoryRepository memoryRepository;
    private final MemoryDetailRepository memoryDetailRepository;

    @Transactional
    public Memory save(Member writer, MemoryCreateRequest request) {
        MemoryDetail memoryDetail = getMemoryDetail(request);
        if (memoryDetail != null) {
            memoryDetail = memoryDetailRepository.save(memoryDetail);
        }
        Pool pool = poolRepository.findById(request.getPoolId()).orElse(null);
        Memory memory =
                Memory.builder()
                        .member(writer)
                        .pool(pool)
                        .memoryDetail(memoryDetail)
                        .recordAt(request.getRecordAt())
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .lane(request.getLane())
                        .diary(request.getDiary())
                        .build();
        return memoryRepository.save(memory);
    }

    @Override
    public MemoryResponse findById(Long memoryId) {
        Memory memory =
                memoryRepository
                        .findById(memoryId)
                        .orElseThrow(() -> new NotFoundException(MemoryErrorType.NOT_FOUND));
        return MemoryResponse.from(memory);
    }

    private MemoryDetail getMemoryDetail(MemoryCreateRequest memoryCreateRequest) {
        if (memoryCreateRequest.getItem() == null
                && memoryCreateRequest.getHeartRate() == null
                && memoryCreateRequest.getPace() == null
                && memoryCreateRequest.getKcal() == null) {
            return null;
        }
        return MemoryDetail.builder()
                .item(memoryCreateRequest.getItem())
                .heartRate(memoryCreateRequest.getHeartRate())
                .pace(memoryCreateRequest.getPace())
                .kcal(memoryCreateRequest.getKcal())
                .build();
    }
}
