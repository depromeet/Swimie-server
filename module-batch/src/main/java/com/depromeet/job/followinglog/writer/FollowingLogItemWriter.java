package com.depromeet.job.followinglog.writer;

import com.depromeet.followinglog.entity.FollowingMemoryLogEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@StepScope
@Component
@RequiredArgsConstructor
public class FollowingLogItemWriter implements ItemWriter<FollowingMemoryLogEntity> {
    @PersistenceContext EntityManager em;

    @Override
    public void write(Chunk<? extends FollowingMemoryLogEntity> chunk) throws Exception {
        List<? extends FollowingMemoryLogEntity> followingMemoryLogEntities = chunk.getItems();
        List<Long> followingMemoryLogIds =
                followingMemoryLogEntities.stream().map(FollowingMemoryLogEntity::getId).toList();

        if (!followingMemoryLogIds.isEmpty()) {
            em.createQuery("DELETE FROM FollowingMemoryLogEntity f WHERE f.id IN :ids")
                    .setParameter("ids", followingMemoryLogIds)
                    .executeUpdate();
        }
    }
}
