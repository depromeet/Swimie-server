package com.depromeet.memory.service;

import com.depromeet.exception.UnauthorizedException;
import com.depromeet.member.Member;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.repository.MemoryDetailRepository;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.security.AuthorizationUtil;
import com.depromeet.type.member.MemberErrorType;
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

    @Transactional
    public Memory save(MemoryCreateRequest memoryCreateRequest) {
        Long loginId = authorizationUtil.getLoginId();
        Member writer =
                memberRepository
                        .findById(loginId)
                        .orElseThrow(() -> new UnauthorizedException(MemberErrorType.NOT_FOUND));
        MemoryDetail memoryDetail = getMemoryDetail(memoryCreateRequest);
        if (memoryDetail != null) {
            memoryDetailRepository.save(memoryDetail);
        }
        Memory memory =
                Memory.builder()
                        .member(writer)
                        .pool(null) // Pool Repository와 findById로 연결하기
                        .memoryDetail(memoryDetail)
                        .recordAt(memoryCreateRequest.getRecordAt())
                        .startTime(memoryCreateRequest.getStartTime())
                        .endTime(memoryCreateRequest.getEndTime())
                        .lane(memoryCreateRequest.getLane())
                        .diary(memoryCreateRequest.getDiary())
                        .build();
        return memoryRepository.save(memory);
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
