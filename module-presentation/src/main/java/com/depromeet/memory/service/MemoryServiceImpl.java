package com.depromeet.memory.service;

import com.depromeet.exception.ForbiddenException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.exception.UnauthorizedException;
import com.depromeet.member.Member;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.repository.MemoryDetailRepository;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.pool.Pool;
import com.depromeet.pool.repository.PoolRepository;
import com.depromeet.security.AuthorizationUtil;
import com.depromeet.type.member.MemberErrorType;
import com.depromeet.type.memory.MemoryErrorType;
import com.depromeet.type.pool.PoolErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemoryServiceImpl implements MemoryService {
    private final MemoryRepository memoryRepository;
    private final MemoryDetailRepository memoryDetailRepository;

    private final MemberRepository memberRepository;
    private final AuthorizationUtil authorizationUtil;

    private final PoolRepository poolRepository;

    @Transactional
    public Memory save(MemoryCreateRequest memoryCreateRequest) {
        Long loginId = authorizationUtil.getLoginId();
        Member writer =
                memberRepository
                        .findById(loginId)
                        .orElseThrow(() -> new UnauthorizedException(MemberErrorType.NOT_FOUND));
        MemoryDetail memoryDetail = getMemoryDetail(memoryCreateRequest);
        if (memoryDetail != null) {
            memoryDetail = memoryDetailRepository.save(memoryDetail);
        }
        Pool pool = null;
        if (memoryCreateRequest.getPoolId() != null) {
            pool =
                    poolRepository
                            .findById(memoryCreateRequest.getPoolId())
                            .orElseThrow(() -> new NotFoundException(PoolErrorType.NOT_FOUND));
        }
        Memory memory =
                Memory.builder()
                        .member(writer)
                        .pool(pool)
                        .memoryDetail(memoryDetail)
                        .recordAt(memoryCreateRequest.getRecordAt())
                        .startTime(memoryCreateRequest.getStartTime())
                        .endTime(memoryCreateRequest.getEndTime())
                        .lane(memoryCreateRequest.getLane())
                        .diary(memoryCreateRequest.getDiary())
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

        validateMemoryMemberMismatch(memory);

        // MemoryDetail 수정
        MemoryDetail updateMemoryDetail =
                MemoryDetail.builder()
                        .item(memoryUpdateRequest.getItem())
                        .heartRate(memoryUpdateRequest.getHeartRate())
                        .pace(memoryUpdateRequest.getPace())
                        .kcal(memoryUpdateRequest.getKcal())
                        .build();
        if (memory.getMemoryDetail() != null) {
            updateMemoryDetail = memory.getMemoryDetail().update(updateMemoryDetail);
        }
        memoryDetailRepository.save(updateMemoryDetail);

        // Pool 정보 찾기
        Pool updatePool = memory.getPool();
        if (memoryUpdateRequest.getPoolId() != null) {
            updatePool =
                    poolRepository
                            .findById(memoryUpdateRequest.getPoolId())
                            .orElseThrow(() -> new NotFoundException(PoolErrorType.NOT_FOUND));
        }

        // memory 수정
        Memory updateMemory =
                Memory.builder()
                        .pool(updatePool)
                        .memoryDetail(updateMemoryDetail)
                        .strokes(strokes)
                        .recordAt(memoryUpdateRequest.getRecordAt())
                        .startTime(memoryUpdateRequest.getStartTime())
                        .endTime(memoryUpdateRequest.getEndTime())
                        .lane(memoryUpdateRequest.getLane())
                        .diary(memoryUpdateRequest.getDiary())
                        .build();
        memory = memory.update(updateMemory);
        return memoryRepository.save(memory);
    }

    private void validateMemoryMemberMismatch(Memory memory) {
        Long loginId = authorizationUtil.getLoginId();
        if (!memory.getMember().getId().equals(loginId)) {
            throw new ForbiddenException(MemoryErrorType.UPDATE_FAILED);
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
}
