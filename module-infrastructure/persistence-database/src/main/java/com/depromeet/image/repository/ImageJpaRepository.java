package com.depromeet.image.repository;

import com.depromeet.image.entity.ImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByMemoryId(Long memoryImageId);

    @Query(
            value =
                    """
            select image \
            from ImageEntity image \
            where image.id = :ids""")
    List<ImageEntity> findAllByIds(List<Long> ids);
}
