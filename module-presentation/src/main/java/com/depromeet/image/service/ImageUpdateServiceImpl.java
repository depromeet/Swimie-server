package com.depromeet.image.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.image.Image;
import com.depromeet.image.dto.response.ImageUploadResponseDto;
import com.depromeet.image.repository.ImageRepository;
import com.depromeet.memory.ImageUploadStatus;
import com.depromeet.memory.Memory;
import com.depromeet.type.image.ImageErrorType;
import com.depromeet.util.ImageNameUtil;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageUpdateServiceImpl implements ImageUpdateService {
    private final ImageRepository imageRepository;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud-front.domain}")
    private String domain;

    @Override
    public List<ImageUploadResponseDto> updateImages(Memory memory, List<String> imageNames) {
        validateImageIsNotNull(imageNames);

        List<Image> existingImages = imageRepository.findAllByMemoryId(memory.getId());
        List<String> existingImageNames = getExistingImageNames(existingImages);

        List<ImageUploadResponseDto> updatedImageNames =
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
        imageRepository.updateByImageIds(imageIds);
    }

    private List<String> getExistingImageNames(List<Image> existingImages) {
        return existingImages.stream().map(Image::getImageName).toList();
    }

    private List<ImageUploadResponseDto> getImageUploadResponseDto(
            Memory memory, List<String> imageNames, List<String> existImagesName) {
        List<ImageUploadResponseDto> imageUploadResponseDtos = new ArrayList<>();

        for (String originImageName : imageNames) {
            String imageName = generateImageName(originImageName);

            if (existImagesName.contains(imageName)) continue;
            String presignedUrl = getPresignedImageUrl(imageName);
            Long addedImageId = saveNewImage(originImageName, imageName, memory);

            ImageUploadResponseDto imageUploadResponseDto =
                    ImageUploadResponseDto.builder()
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
        return imageRepository.save(newImage);
    }

    private String getPresignedImageUrl(String imageName) {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder().bucket(bucketName).key(imageName).build();

        PutObjectPresignRequest putObjectPresignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(5))
                        .putObjectRequest(putObjectRequest)
                        .build();

        PresignedPutObjectRequest presignedPutObjectRequest =
                s3Presigner.presignPutObject(putObjectPresignRequest);

        return presignedPutObjectRequest.url().toString();
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
            imageRepository.deleteAllByIds(deletedImageIds);
        }
    }
}
