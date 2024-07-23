package com.depromeet.memory.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.Member;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.repository.MemoryDetailRepository;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.pool.Pool;
import com.depromeet.pool.repository.PoolRepository;
import com.depromeet.type.memory.MemoryDetailErrorType;
import com.depromeet.type.memory.MemoryErrorType;
import com.depromeet.type.pool.PoolErrorType;
import java.util.List;
import java.util.Optional;
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
        checkMemoryAlreadyExist(request);

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
        if (memory == null) {
            throw new InternalServerException(MemoryErrorType.CREATE_FAILED);
        }
        return memoryRepository.save(memory);
    }

    @Override
    public Memory findById(Long memoryId) {
        return memoryRepository
                .findById(memoryId)
                .orElseThrow(() -> new NotFoundException(MemoryErrorType.NOT_FOUND));
    }

    @Override
    @Transactional
    public Memory update(
            Long memoryId, MemoryUpdateRequest memoryUpdateRequest, List<Stroke> strokes) {
        Memory memory = findById(memoryId);

        MemoryDetail updateMemoryDetail = updateMemoryDetail(memoryUpdateRequest, memory);
        Pool updatePool = getUpdatePool(memoryUpdateRequest.getPoolId(), memory.getPool());

        // Memory 수정
        Memory updateMemory =
                Memory.builder()
                        .member(memory.getMember())
                        .pool(updatePool)
                        .memoryDetail(updateMemoryDetail)
                        .strokes(strokes)
                        .recordAt(memoryUpdateRequest.getRecordAt())
                        .startTime(memoryUpdateRequest.getStartTime())
                        .endTime(memoryUpdateRequest.getEndTime())
                        .lane(memoryUpdateRequest.getLane())
                        .diary(memoryUpdateRequest.getDiary())
                        .build();

        return memoryRepository
                .update(memoryId, updateMemory)
                .orElseThrow(() -> new NotFoundException(MemoryErrorType.NOT_FOUND));
    }

    private void checkMemoryAlreadyExist(MemoryCreateRequest request) {
        Optional<Memory> memoryByRecordAt = memoryRepository.findByRecordAt(request.getRecordAt());

        if (memoryByRecordAt.isPresent()) {
            throw new BadRequestException(MemoryErrorType.ALREADY_CREATED);
        }
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

    private MemoryDetail updateMemoryDetail(
            MemoryUpdateRequest memoryUpdateRequest, Memory memory) {
        MemoryDetail updateMemoryDetail =
                MemoryDetail.builder()
                        .item(memoryUpdateRequest.getItem())
                        .heartRate(memoryUpdateRequest.getHeartRate())
                        .pace(memoryUpdateRequest.getPace())
                        .kcal(memoryUpdateRequest.getKcal())
                        .build();
        if (memory.getMemoryDetail() != null) {
            Long memoryDetailId = memory.getMemoryDetail().getId();
            updateMemoryDetail =
                    memoryDetailRepository
                            .update(memoryDetailId, updateMemoryDetail)
                            .orElseThrow(
                                    () -> new NotFoundException(MemoryDetailErrorType.NOT_FOUND));
        } else {
            updateMemoryDetail = memoryDetailRepository.save(updateMemoryDetail);
        }
        return updateMemoryDetail;
    }

    private Pool getUpdatePool(Long poolId, Pool pool) {
        if (poolId != null) {
            pool =
                    poolRepository
                            .findById(poolId)
                            .orElseThrow(() -> new NotFoundException(PoolErrorType.NOT_FOUND));
        }
        return pool;
    }
}
