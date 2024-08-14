package com.depromeet.image.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.image.domain.vo.ImagePresignedUrlNameVo;
import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.depromeet.image.port.in.ImageUploadUseCase;
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
public class ImageUploadService implements ImageUploadUseCase {
    private final ImagePersistencePort imagePersistencePort;
    private final S3ManagePort s3ManagePort;
    private final Clock clock;

    @Value("${cloud-front.domain}")
    private String domain;

    @Override
    public List<ImagePresignedUrlVo> getPresignedUrlAndSaveImages(List<String> originImageNames) {
        validateImagesIsNotEmpty(originImageNames);

        List<ImagePresignedUrlVo> imageResponses = new ArrayList<>();
        for (String originImageName : originImageNames) {
            String imageName =
                    ImageNameUtil.createImageName(originImageName, LocalDateTime.now(clock));
            String contentType = ImageNameUtil.getContentType(originImageName);

            String imagePresignedUrl = s3ManagePort.getPresignedUrl(imageName, contentType);

            Long imageId = saveImage(originImageName, imageName);
            ImagePresignedUrlVo imagePresignedUrlVo =
                    getImageUploadResponseDto(imageId, originImageName, imagePresignedUrl);
            imageResponses.add(imagePresignedUrlVo);
        }
        return imageResponses;
    }

    private void validateImagesIsNotEmpty(List<String> originImageNames) {
        if (originImageNames.isEmpty()) {
            throw new BadRequestException(ImageErrorType.IMAGES_CANNOT_BE_EMPTY);
        }
    }

    private Long saveImage(String originImageName, String imageNames) {
        Image image =
                Image.builder()
                        .originImageName(originImageName)
                        .imageName(imageNames)
                        .imageUrl(domain + "/" + imageNames)
                        .imageUploadStatus(ImageUploadStatus.PENDING)
                        .build();
        return imagePersistencePort.save(image);
    }

    private ImagePresignedUrlVo getImageUploadResponseDto(
            Long imageId, String imageName, String imagePresignedUrl) {
        return ImagePresignedUrlVo.builder()
                .imageId(imageId)
                .imageName(imageName)
                .presignedUrl(imagePresignedUrl)
                .build();
    }

    @Override
    public ImagePresignedUrlNameVo getPresignedUrlAndSaveProfileImage(String originImageName) {
        String imageName = ImageNameUtil.createImageName(originImageName, LocalDateTime.now(clock));
        String contentType = ImageNameUtil.getContentType(originImageName);
        String imagePresignedUrl = s3ManagePort.getPresignedUrl(imageName, contentType);
        return getImageUploadResponseDto(originImageName, imageName, imagePresignedUrl);
    }

    private ImagePresignedUrlNameVo getImageUploadResponseDto(
            String originImageName, String imageName, String imagePresignedUrl) {
        return ImagePresignedUrlNameVo.builder()
                .originImageName(originImageName)
                .imageName(imageName)
                .presignedUrl(imagePresignedUrl)
                .build();
    }

    @Override
    public void changeImageStatusAndAddMemoryIdToImages(Memory memory, List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) return;
        List<Image> images = imagePersistencePort.findImageByIds(imageIds);
        for (Image image : images) {
            image.addMemoryToImage(memory);
            image.updateHasUploaded();
        }
        imagePersistencePort.saveAll(images);
    }
}
