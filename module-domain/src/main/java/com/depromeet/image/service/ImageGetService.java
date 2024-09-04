package com.depromeet.image.service;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.vo.MemoryImageUrlVo;
import com.depromeet.image.port.in.ImageGetUseCase;
import com.depromeet.image.port.out.persistence.ImagePersistencePort;
import java.util.List;
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
    public List<MemoryImageUrlVo> findImagesByMemoryIds(List<Long> memoryIds) {
        return imagePersistencePort.findByImageByMemoryIds(memoryIds);
    }
}
