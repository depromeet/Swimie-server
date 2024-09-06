package com.depromeet.image.service;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.vo.MemoryImageUrlVo;
import com.depromeet.image.port.in.ImageGetUseCase;
import com.depromeet.image.port.out.persistence.ImagePersistencePort;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageGetService implements ImageGetUseCase {
    private final ImagePersistencePort imagePersistencePort;

    @Override
    public List<Image> findImagesByMemoryId(Long memoryId) {
        return imagePersistencePort.findAllByMemoryIdAndHasUploaded(memoryId);
    }

    @Override
    public Map<Long, MemoryImageUrlVo> findImagesByMemoryIds(List<Long> memoryIds) {
        List<MemoryImageUrlVo> imageUrlVos = imagePersistencePort.findByImageByMemoryIds(memoryIds);

        return imageUrlVos.stream()
                .collect(
                        Collectors.toMap(
                                MemoryImageUrlVo::memoryId,
                                Function.identity(),
                                (imageUrlVo1, imageUrlVo2) ->
                                        imageUrlVo1.imageId() < imageUrlVo2.imageId()
                                                ? imageUrlVo1
                                                : imageUrlVo2));
    }
}
