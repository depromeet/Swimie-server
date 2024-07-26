package com.depromeet.image.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.image.port.in.ImageUploadUseCase;
import com.depromeet.image.port.out.command.ImagePresignedUrlCommand;
import com.depromeet.image.port.out.persistence.ImagePersistencePort;
import com.depromeet.image.port.out.s3.S3ManagePort;
import com.depromeet.memory.Memory;
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
public class ImageUploadService implements ImageUploadUseCase {
    private final ImagePersistencePort imagePersistencePort;
    private final S3ManagePort s3ManagePort;

    @Value("${cloud-front.domain}")
    private String domain;

    @Override
    public List<ImagePresignedUrlCommand> getPresignedUrlAndSaveImages(
            List<String> originImageNames) {
        validateImagesIsNotEmpty(originImageNames);

        List<ImagePresignedUrlCommand> imageResponses = new ArrayList<>();
        for (String originImageName : originImageNames) {
            String imageName = getImageName(originImageName);
            String imagePresignedUrl = s3ManagePort.getPresignedUrl(imageName);

            Long imageId = saveImage(originImageName, imageName);
            ImagePresignedUrlCommand imagePresignedUrlCommand =
                    getImageUploadResponseDto(imageId, originImageName, imagePresignedUrl);
            imageResponses.add(imagePresignedUrlCommand);
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

    private ImagePresignedUrlCommand getImageUploadResponseDto(
            Long imageId, String imageName, String imagePresignedUrl) {
        return ImagePresignedUrlCommand.builder()
                .imageId(imageId)
                .imageName(imageName)
                .presignedUrl(imagePresignedUrl)
                .build();
    }

    @Override
    public void changeImageStatusAndAddMemoryIdToImages(Memory memory, List<Long> imageIds) {
        if (imageIds.isEmpty()) return;
        List<Image> images = imagePersistencePort.findImageByIds(imageIds);
        for (Image image : images) {
            image.addMemoryToImage(memory);
            image.updateHasUploaded();
        }
        imagePersistencePort.saveAll(images);
    }
}
