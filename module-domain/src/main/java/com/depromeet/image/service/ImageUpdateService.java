package com.depromeet.image.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.depromeet.image.port.in.ImageUpdateUseCase;
import com.depromeet.image.port.out.persistence.ImagePersistencePort;
import com.depromeet.image.port.out.s3.S3ManagePort;
import com.depromeet.memory.domain.Memory;
import com.depromeet.type.image.ImageErrorType;
import com.depromeet.util.ImageNameUtil;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageUpdateService implements ImageUpdateUseCase {
    private final ImagePersistencePort imagePersistencePort;
    private final S3ManagePort s3ManagePort;
    private final Clock clock;

    @Value("${cloud-front.domain}")
    private String domain;

    @Override
    public List<ImagePresignedUrlVo> updateImages(Memory memory, List<String> newImageNames) {
        validateImageIsNotNull(newImageNames);

        List<Image> existingImages = imagePersistencePort.findAllByMemoryId(memory.getId());
        List<String> existingImageNames = getExistingImageNames(existingImages);

        List<ImagePresignedUrlVo> updatedImageNames =
                getImageUploadResponseDto(memory, newImageNames, existingImageNames);
        deleteNonUpdatedImages(existingImages, newImageNames);
        return updatedImageNames;
    }

    private void validateImageIsNotNull(List<String> imageNames) {
        if (imageNames == null) {
            throw new BadRequestException(ImageErrorType.IMAGES_CANNOT_BE_NULL);
        }
        for (String imageName : imageNames) {
            if (imageName == null || imageName.isEmpty()) {
                throw new BadRequestException(ImageErrorType.INVALID_IMAGE_NAME);
            }
        }
    }

    @Override
    public void changeImageStatus(List<Long> imageIds) {
        imagePersistencePort.updateByImageIds(imageIds);
    }

    private List<String> getExistingImageNames(List<Image> existingImages) {
        return existingImages.stream().map(Image::getImageName).toList();
    }

    private List<ImagePresignedUrlVo> getImageUploadResponseDto(
            Memory memory, List<String> newImageNames, List<String> existImagesNames) {
        List<ImagePresignedUrlVo> imageUploadResponseDtos = new ArrayList<>();

        for (String imageName : newImageNames) {
            validateImageName(imageName);
            boolean isImageExist =
                    ImageNameUtil.validateImageNameIsUUID(
                            imageName); // image가 uuid 인지 확인 (기존의 이미지인지 확인)

            if (isImageExist && existImagesNames.contains(imageName)) continue;
            String uuidImageName =
                    ImageNameUtil.createImageName(imageName, LocalDateTime.now(clock));
            String contentType = ImageNameUtil.getContentType(imageName);

            String presignedUrl = s3ManagePort.getPresignedUrl(uuidImageName, contentType);
            Long addedImageId = saveNewImage(imageName, uuidImageName, memory);

            ImagePresignedUrlVo imageUploadResponseDto =
                    ImagePresignedUrlVo.builder()
                            .imageId(addedImageId)
                            .imageName(imageName)
                            .presignedUrl(presignedUrl)
                            .build();
            imageUploadResponseDtos.add(imageUploadResponseDto);
        }
        return imageUploadResponseDtos;
    }

    private void validateImageName(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            throw new BadRequestException(ImageErrorType.INVALID_IMAGE_NAME);
        }
    }

    private Long saveNewImage(String originImageName, String imageName, Memory memory) {
        Image newImage =
                Image.builder()
                        .originImageName(originImageName)
                        .imageName(imageName)
                        .imageUrl(domain + "/" + imageName)
                        .memory(memory)
                        .imageUploadStatus(ImageUploadStatus.PENDING)
                        .build();
        return imagePersistencePort.save(newImage);
    }

    private void deleteNonUpdatedImages(List<Image> existImages, List<String> updatedImageNames) {
        List<Long> deletedImageIds =
                existImages.stream()
                        .filter(i -> !updatedImageNames.contains(i.getImageName()))
                        .map(Image::getId)
                        .toList();
        if (!deletedImageIds.isEmpty()) {
            imagePersistencePort.deleteAllByIds(deletedImageIds);
        }
    }
}
