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

    @Value("${cloud-front.domain}")
    private String domain;

    @Override
    public List<ImagePresignedUrlVo> updateImages(Memory memory, List<String> imageNames) {
        validateImageIsNotNull(imageNames);

        List<Image> existingImages = imagePersistencePort.findAllByMemoryId(memory.getId());
        List<String> existingImageNames = getExistingImageNames(existingImages);

        List<ImagePresignedUrlVo> updatedImageNames =
                getImageUploadResponseDto(memory, imageNames, existingImageNames);
        deleteNonUpdatedImages(existingImages, imageNames);
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
            Memory memory, List<String> imageNames, List<String> existImagesName) {
        List<ImagePresignedUrlVo> imageUploadResponseDtos = new ArrayList<>();

        for (String originImageName : imageNames) {
            String imageName = generateImageName(originImageName);

            if (existImagesName.contains(imageName)) continue;
            String presignedUrl = s3ManagePort.getPresignedUrl(imageName);
            Long addedImageId = saveNewImage(originImageName, imageName, memory);

            ImagePresignedUrlVo imageUploadResponseDto =
                    ImagePresignedUrlVo.builder()
                            .imageId(addedImageId)
                            .imageName(originImageName)
                            .presignedUrl(presignedUrl)
                            .build();
            imageUploadResponseDtos.add(imageUploadResponseDto);
        }
        return imageUploadResponseDtos;
    }

    private String generateImageName(String originImageName) {
        if (originImageName == null || originImageName.isEmpty()) {
            throw new BadRequestException(ImageErrorType.INVALID_IMAGE_NAME);
        }

        return ImageNameUtil.createImageName(originImageName);
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

    private void deleteNonUpdatedImages(
            List<Image> existImages, List<String> updateOriginImageNames) {
        List<String> updatedImageNames =
                updateOriginImageNames.stream().map(ImageNameUtil::createImageName).toList();

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
