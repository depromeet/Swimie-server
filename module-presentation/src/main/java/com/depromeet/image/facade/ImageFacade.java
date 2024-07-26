package com.depromeet.image.facade;

import com.depromeet.image.dto.response.ImageUploadResponseDto;
import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.image.service.ImageDeleteService;
import com.depromeet.image.service.ImageGetService;
import com.depromeet.image.service.ImageUpdateService;
import com.depromeet.image.service.ImageUploadService;
import com.depromeet.memory.Memory;
import com.depromeet.memory.service.MemoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageFacade {
    private final ImageUploadService imageUploadService;
    private final ImageUpdateService imageUpdateService;
    private final ImageGetService imageGetService;
    private final ImageDeleteService imageDeleteService;
    private final MemoryService memoryService;

    public List<ImageUploadResponseDto> getPresignedUrlAndSaveImages(List<String> imageNames) {
        return imageUploadService.getPresignedUrlAndSaveImages(imageNames);
    }

    public List<ImageUploadResponseDto> updateImages(Long memoryId, List<String> imageNames) {
        Memory memory = memoryService.findById(memoryId);
        return imageUpdateService.updateImages(memory, imageNames);
    }

    public void changeImageStatus(List<Long> imageIds) {
        imageUpdateService.changeImageStatus(imageIds);
    }

    public List<MemoryImagesDto> findImagesByMemoryId(Long memoryId) {
        return imageGetService.findImagesByMemoryId(memoryId);
    }

    public void deleteAllImagesByMemoryId(Long memoryId) {
        imageDeleteService.deleteAllImagesByMemoryId(memoryId);
    }
}
