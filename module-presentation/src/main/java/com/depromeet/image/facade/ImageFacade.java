package com.depromeet.image.facade;

import com.depromeet.image.domain.Image;
import com.depromeet.image.dto.response.ImageUploadResponseDto;
import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.image.port.in.ImageDeleteUseCase;
import com.depromeet.image.port.in.ImageGetUseCase;
import com.depromeet.image.port.in.ImageUpdateUseCase;
import com.depromeet.image.port.in.ImageUploadUseCase;
import com.depromeet.image.port.out.command.ImagePresignedUrlCommand;
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
    private final ImageUploadUseCase imageUploadUseCase;
    private final ImageUpdateUseCase imageUpdateUseCase;
    private final ImageGetUseCase imageGetUseCase;
    private final ImageDeleteUseCase imageDeleteUseCase;
    private final MemoryService memoryService;

    public List<ImageUploadResponseDto> getPresignedUrlAndSaveImages(List<String> imageNames) {
        List<ImagePresignedUrlCommand> imagePresignedUrlCommands =
                imageUploadUseCase.getPresignedUrlAndSaveImages(imageNames);
        return imagePresignedUrlCommands.stream().map(ImageUploadResponseDto::of).toList();
    }

    public List<ImageUploadResponseDto> updateImages(Long memoryId, List<String> imageNames) {
        Memory memory = memoryService.findById(memoryId);
        List<ImagePresignedUrlCommand> imagePresignedUrlCommands =
                imageUpdateUseCase.updateImages(memory, imageNames);
        return imagePresignedUrlCommands.stream().map(ImageUploadResponseDto::of).toList();
    }

    public void changeImageStatus(List<Long> imageIds) {
        imageUpdateUseCase.changeImageStatus(imageIds);
    }

    public List<MemoryImagesDto> findImagesByMemoryId(Long memoryId) {
        List<Image> images = imageGetUseCase.findImagesByMemoryId(memoryId);
        return images.stream()
                .map(
                        image ->
                                MemoryImagesDto.builder()
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
