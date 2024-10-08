package com.depromeet.job.followinglog.reader;

import com.depromeet.followinglog.entity.FollowingMemoryLogEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class FollowingLogItemReader implements ItemReader<FollowingMemoryLogEntity> {
    @PersistenceContext private EntityManager em;
    private static final int PAGE_SIZE = 10;
    private int currentIndex = 0;
    private List<FollowingMemoryLogEntity> followingMemoryLogEntities;

    @Override
    public FollowingMemoryLogEntity read() {
        if (followingMemoryLogEntities == null
                || currentIndex >= followingMemoryLogEntities.size()) {
            fetchNextPage();
        }
        if (followingMemoryLogEntities != null
                && currentIndex < followingMemoryLogEntities.size()) {
            return followingMemoryLogEntities.get(currentIndex++);
        }
        return null;
    }

    private void fetchNextPage() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before100Days = now.minusDays(100);

        TypedQuery<FollowingMemoryLogEntity> query =
                em.createQuery(
                        "SELECT f FROM FollowingMemoryLogEntity f WHERE f.createdAt < :date",
                        FollowingMemoryLogEntity.class);
        query.setParameter("date", before100Days);
        query.setFirstResult(currentIndex);
        query.setMaxResults(PAGE_SIZE);
        followingMemoryLogEntities = query.getResultList();
        currentIndex = 0;
    }
}
