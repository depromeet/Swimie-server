package com.depromeet.memory;

import static com.depromeet.type.member.MemberSuccessType.GET_SUCCESS;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.memory.repository.MemoryDetailRepository;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.pool.Pool;
import com.depromeet.pool.repository.PoolRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// image api 테스트를 위해 임시로 생성
@RestController
@RequestMapping("/api/v1/memory")
@RequiredArgsConstructor
public class MemoryTempController {
    private final MemoryRepository memoryRepository;
    private final MemoryDetailRepository memoryDetailRepository;
    private final MemberRepository memberRepository;
    private final PoolRepository poolRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> save() {
        MemoryDetail memoryDetail =
                MemoryDetail.builder()
                        .heartRate((short) 100)
                        .item("항공모함")
                        .kcal(100)
                        .pace(LocalTime.of(7, 0))
                        .build();
        memoryDetail = memoryDetailRepository.save(memoryDetail);
        Pool pool = Pool.builder().name("수영장").lane(250).build();
        pool = poolRepository.save(pool);
        Memory memory =
                Memory.builder()
                        .member(memberRepository.findById(1L).get())
                        .diary("test diary")
                        .memoryDetail(memoryDetail)
                        .recordAt(LocalDate.now())
                        .startTime(LocalTime.of(9, 0))
                        .endTime(LocalTime.of(11, 0))
                        .pool(pool)
                        .build();
        memory = memoryRepository.save(memory);

        return ResponseEntity.ok(ApiResponse.success(GET_SUCCESS, memory.getId()));
    }
}
