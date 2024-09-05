package com.depromeet.memory.repository;

import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.memory.entity.MemoryDetailEntity;
import com.depromeet.memory.entity.QMemoryDetailEntity;
import com.depromeet.memory.port.out.persistence.MemoryDetailPersistencePort;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemoryDetailRepository implements MemoryDetailPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final MemoryDetailJpaRepository memoryDetailJpaRepository;

    private QMemoryDetailEntity memoryDetail = QMemoryDetailEntity.memoryDetailEntity;

    @Override
    public MemoryDetail save(MemoryDetail memoryDetail) {
        return memoryDetailJpaRepository.save(MemoryDetailEntity.from(memoryDetail)).toModel();
    }

    @Override
    public Optional<MemoryDetail> update(Long id, MemoryDetail updateMemoryDetail) {
        return memoryDetailJpaRepository
                .findById(id)
                .map(
                        entity ->
                                entity.update(MemoryDetailEntity.from(updateMemoryDetail))
                                        .toModel());
    }

    @Override
    public void deleteAllById(List<Long> memoryDetailIds) {
        queryFactory.delete(memoryDetail).where(memoryDetail.id.in(memoryDetailIds)).execute();
    }

    @Override
    public void deleteById(Long removeTargetId) {
        memoryDetailJpaRepository.deleteById(removeTargetId);
    }
}
