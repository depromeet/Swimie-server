package com.depromeet.job.image.writer;

import com.depromeet.image.entity.ImageEntity;
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
public class ImageItemWriter implements ItemWriter<ImageEntity> {
    @PersistenceContext EntityManager em;

    @Override
    public void write(Chunk<? extends ImageEntity> chunk) throws Exception {
        List<? extends ImageEntity> imageEntities = chunk.getItems();
        List<Long> imageIds = imageEntities.stream().map(ImageEntity::getId).toList();

        if (!imageIds.isEmpty()) {
            em.createQuery("DELETE FROM ImageEntity i WHERE i.id IN :ids")
                    .setParameter("ids", imageIds)
                    .executeUpdate();
        }
    }
}
