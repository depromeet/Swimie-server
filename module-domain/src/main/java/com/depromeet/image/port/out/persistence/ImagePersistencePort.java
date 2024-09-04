package com.depromeet.image.port.out.persistence;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.vo.MemoryImageUrlVo;
import java.util.List;
import java.util.Optional;

public interface ImagePersistencePort {
    Long save(Image image);

    List<Image> saveAll(List<Image> images);

    void updateByImageIds(List<Long> imageIds);

    List<MemoryImageUrlVo> findByImageByMemoryIds(List<Long> memoryIds);

    Optional<Image> findById(Long id);

    List<Image> findAllByMemoryId(Long memoryId);

    List<Image> findAllByMemoryIdAndHasUploaded(Long memoryId);

    List<Image> findImageByIds(List<Long> ids);

    void deleteById(Long id);

    void deleteAllByIds(List<Long> ids);

    void deleteAllByMemoryId(Long memoryId);

    void setNullByMemoryIds(List<Long> memoryIds);
}
