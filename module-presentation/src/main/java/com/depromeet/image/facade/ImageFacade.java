package com.depromeet.image.facade;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.depromeet.image.dto.response.ImageUploadResponse;
import com.depromeet.image.dto.response.MemoryImagesResponse;
import com.depromeet.image.port.in.ImageDeleteUseCase;
import com.depromeet.image.port.in.ImageGetUseCase;
import com.depromeet.image.port.in.ImageUpdateUseCase;
import com.depromeet.image.port.in.ImageUploadUseCase;
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
    private final MemoryService memoryService;
    private final ImageGetUseCase imageGetUseCase;
    private final ImageUploadUseCase imageUploadUseCase;
    private final ImageUpdateUseCase imageUpdateUseCase;
    private final ImageDeleteUseCase imageDeleteUseCase;

    public List<ImageUploadResponse> getPresignedUrlAndSaveImages(List<String> imageNames) {
        List<ImagePresignedUrlVo> imagePresignedUrlVos =
                imageUploadUseCase.getPresignedUrlAndSaveImages(imageNames);
        return imagePresignedUrlVos.stream().map(ImageUploadResponse::of).toList();
    }

    public List<ImageUploadResponse> updateImages(Long memoryId, List<String> imageNames) {
        Memory memory = memoryService.findById(memoryId);
        List<ImagePresignedUrlVo> imagePresignedUrlVos =
                imageUpdateUseCase.updateImages(memory, imageNames);
        return imagePresignedUrlVos.stream().map(ImageUploadResponse::of).toList();
    }

    public void changeImageStatus(List<Long> imageIds) {
        imageUpdateUseCase.changeImageStatus(imageIds);
    }

    public List<MemoryImagesResponse> findImagesByMemoryId(Long memoryId) {
        List<Image> images = imageGetUseCase.findImagesByMemoryId(memoryId);
        return images.stream()
                .map(
                        image ->
                                MemoryImagesResponse.builder()
                                        .imageId(image.getId())
                                        .originImageName(image.getOriginImageName())
                                        .imageName(image.getImageName())
                                        .url(image.getImageUrl())
                                        .build())
                .toList();
    }

    public void deleteAllImagesByMemoryId(Long memoryId) {
        imageDeleteUseCase.deleteAllImagesByMemoryId(memoryId);
    }
}
