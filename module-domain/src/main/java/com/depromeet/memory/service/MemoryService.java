package com.depromeet.memory.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.port.in.command.CreateMemoryCommand;
import com.depromeet.memory.port.in.command.UpdateMemoryCommand;
import com.depromeet.memory.port.in.usecase.CreateMemoryUseCase;
import com.depromeet.memory.port.in.usecase.GetMemoryUseCase;
import com.depromeet.memory.port.in.usecase.UpdateMemoryUseCase;
import com.depromeet.memory.port.out.persistence.MemoryDetailPersistencePort;
import com.depromeet.memory.port.out.persistence.MemoryPersistencePort;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.port.out.persistence.PoolPersistencePort;
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
public class MemoryService implements CreateMemoryUseCase, UpdateMemoryUseCase, GetMemoryUseCase {
    private final PoolPersistencePort poolPersistencePort;
    private final MemoryPersistencePort memoryPersistencePort;
    private final MemoryDetailPersistencePort memoryDetailPersistencePort;

    @Transactional
    public Memory save(Member writer, CreateMemoryCommand command) {
        MemoryDetail memoryDetail = getMemoryDetail(command);
        checkMemoryAlreadyExist(command, writer.getId());

        if (memoryDetail != null) {
            memoryDetail = memoryDetailPersistencePort.save(memoryDetail);
        }

        Pool pool = null;
        if (command.poolId() != null) {
            pool = poolPersistencePort.findById(command.poolId()).orElse(null);
        }
        Memory memory =
                Memory.builder()
                        .member(writer)
                        .pool(pool)
                        .memoryDetail(memoryDetail)
                        .recordAt(command.recordAt())
                        .startTime(command.startTime())
                        .endTime(command.endTime())
                        .lane(command.lane())
                        .diary(command.diary())
                        .build();
        if (memory == null) {
            throw new InternalServerException(MemoryErrorType.CREATE_FAILED);
        }
        return memoryPersistencePort.save(memory);
    }

    @Override
    public Memory findById(Long memoryId) {
        return memoryPersistencePort
                .findById(memoryId)
                .orElseThrow(() -> new NotFoundException(MemoryErrorType.NOT_FOUND));
    }

    @Override
    public int findOrderInMonth(Long memberId, Long memoryId, int month) {
        return memoryPersistencePort.findOrderInMonth(memberId, memoryId, month);
    }

    @Override
    @Transactional
    public Memory update(Long memoryId, UpdateMemoryCommand command, List<Stroke> strokes) {
        Memory memory = findById(memoryId);

        MemoryDetail updateMemoryDetail = updateMemoryDetail(command, memory);
        Pool updatePool = getUpdatePool(command.poolId(), memory.getPool());

        // Memory 수정
        Memory updateMemory =
                Memory.builder()
                        .member(memory.getMember())
                        .pool(updatePool)
                        .memoryDetail(updateMemoryDetail)
                        .strokes(strokes)
                        .recordAt(command.recordAt())
                        .startTime(command.startTime())
                        .endTime(command.endTime())
                        .lane(command.lane())
                        .diary(command.diary())
                        .build();

        return memoryPersistencePort
                .update(memoryId, updateMemory)
                .orElseThrow(() -> new NotFoundException(MemoryErrorType.NOT_FOUND));
    }

    private void checkMemoryAlreadyExist(CreateMemoryCommand command, Long memberId) {
        Optional<Memory> memoryByRecordAt =
                memoryPersistencePort.findByRecordAtAndMemberId(command.recordAt(), memberId);

        if (memoryByRecordAt.isPresent()) {
            throw new BadRequestException(MemoryErrorType.ALREADY_CREATED);
        }
    }

    private MemoryDetail getMemoryDetail(CreateMemoryCommand command) {
        if (command.item() == null
                && command.heartRate() == null
                && command.pace() == null
                && command.kcal() == null) {
            return null;
        }
        return MemoryDetail.builder()
                .item(command.item())
                .heartRate(command.heartRate())
                .pace(command.pace())
                .kcal(command.kcal())
                .build();
    }

    private MemoryDetail updateMemoryDetail(UpdateMemoryCommand command, Memory memory) {
        MemoryDetail updateMemoryDetail =
                MemoryDetail.builder()
                        .item(command.item())
                        .heartRate(command.heartRate())
                        .pace(command.pace())
                        .kcal(command.kcal())
                        .build();
        if (memory.getMemoryDetail() != null) {
            Long memoryDetailId = memory.getMemoryDetail().getId();
            updateMemoryDetail =
                    memoryDetailPersistencePort
                            .update(memoryDetailId, updateMemoryDetail)
                            .orElseThrow(
                                    () -> new NotFoundException(MemoryDetailErrorType.NOT_FOUND));
        } else {
            updateMemoryDetail = memoryDetailPersistencePort.save(updateMemoryDetail);
        }
        return updateMemoryDetail;
    }

    private Pool getUpdatePool(Long poolId, Pool pool) {
        if (poolId != null) {
            pool =
                    poolPersistencePort
                            .findById(poolId)
                            .orElseThrow(() -> new NotFoundException(PoolErrorType.NOT_FOUND));
        }
        return pool;
    }
}
