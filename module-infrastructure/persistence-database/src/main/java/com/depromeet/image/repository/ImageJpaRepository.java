package com.depromeet.image.repository;

import com.depromeet.image.entity.ImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByMemoryId(Long memoryId);

    @Query(value = """
    select i from ImageEntity i where i.id in :ids
    """)
    List<ImageEntity> findAllByIds(List<Long> ids);

    void deleteAllByMemoryId(Long memoryId);
}
