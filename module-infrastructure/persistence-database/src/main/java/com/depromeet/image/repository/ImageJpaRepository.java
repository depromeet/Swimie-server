package com.depromeet.image.repository;

import com.depromeet.image.entity.ImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByMemoryId(Long memoryId);

    @Query(value = """
    select i from ImageEntity i where i.id in :ids
    """)
    List<ImageEntity> findAllByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query("delete from ImageEntity i where i.memory.id = :memoryId")
    void deleteAllByMemoryId(@Param("memoryId") Long memoryId);
}
