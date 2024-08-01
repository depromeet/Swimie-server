package com.depromeet.job.image.reader;

import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.image.entity.ImageEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class ImageItemReader implements ItemReader<ImageEntity> {
    @PersistenceContext private EntityManager em;
    private static final int PAGE_SIZE = 10;
    private int currentIndex = 0;
    private List<ImageEntity> images;

    @Override
    public ImageEntity read() throws Exception {
        if (images == null || currentIndex >= images.size()) {
            fetchNextPage();
        }
        if (images != null && currentIndex < images.size()) {
            return images.get(currentIndex++);
        }
        return null;
    }

    private void fetchNextPage() {
        TypedQuery<ImageEntity> query =
                em.createQuery(
                        "SELECT i FROM ImageEntity i WHERE i.imageUploadStatus = :status",
                        ImageEntity.class);
        query.setParameter("status", ImageUploadStatus.PENDING);
        query.setFirstResult(currentIndex);
        query.setMaxResults(PAGE_SIZE);
        images = query.getResultList();
        currentIndex = 0;
    }
}
