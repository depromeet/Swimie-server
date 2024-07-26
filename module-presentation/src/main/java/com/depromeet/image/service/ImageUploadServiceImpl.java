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
public class ImageUploadServiceImpl implements ImageUploadService {
    private final ImageRepository imageRepository;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud-front.domain}")
    private String domain;

    @Override
    public List<ImageUploadResponseDto> getPresignedUrlAndSaveImages(
            List<String> originImageNames) {
        validateImagesIsNotEmpty(originImageNames);

        List<ImageUploadResponseDto> imageResponses = new ArrayList<>();
        for (String originImageName : originImageNames) {
            String imageName = getImageName(originImageName);
            String imagePresignedUrl = getPresignedUrl(originImageName);

            Long imageId = saveImage(originImageName, imageName);
            ImageUploadResponseDto imageUploadResponseDto =
                    getImageUploadResponseDto(imageId, originImageName, imagePresignedUrl);
            imageResponses.add(imageUploadResponseDto);
        }
        return imageResponses;
    }

    private void validateImagesIsNotEmpty(List<String> originImageNames) {
        if (originImageNames.isEmpty()) {
            throw new BadRequestException(ImageErrorType.IMAGES_CANNOT_BE_EMPTY);
        }
    }

    private String getImageName(String originImageName) {
        if (originImageName == null || originImageName.equals("")) {
            throw new BadRequestException(ImageErrorType.INVALID_IMAGE_NAME);
        }
        return ImageNameUtil.createImageName(originImageName);
    }

    @Override
    public void changeImageStatusAndAddMemoryIdToImages(Memory memory, List<Long> imageIds) {
        if (imageIds.isEmpty()) return;
        List<Image> images = imageRepository.findImageByIds(imageIds);
        for (Image image : images) {
            image.addMemoryToImage(memory);
            image.updateHasUploaded();
        }
        imageRepository.saveAll(images);
    }

    private Long saveImage(String originImageName, String imageNames) {
        Image image =
                Image.builder()
                        .originImageName(originImageName)
                        .imageName(imageNames)
                        .imageUrl(domain + "/" + imageNames)
                        .imageUploadStatus(ImageUploadStatus.PENDING)
                        .build();
        return imageRepository.save(image);
    }

    private ImageUploadResponseDto getImageUploadResponseDto(
            Long imageId, String imageName, String imagePresignedUrl) {
        return ImageUploadResponseDto.builder()
                .imageId(imageId)
                .imageName(imageName)
                .presignedUrl(imagePresignedUrl)
                .build();
    }

    public String getPresignedUrl(String imageName) {
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
}
