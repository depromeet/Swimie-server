package com.depromeet.image.service;

import com.depromeet.exception.NotFoundException;
import com.depromeet.image.domain.Image;
import com.depromeet.image.port.in.ImageDeleteUseCase;
import com.depromeet.image.port.out.persistence.ImagePersistencePort;
import com.depromeet.image.port.out.s3.S3ManagePort;
import com.depromeet.type.image.ImageErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageDeleteService implements ImageDeleteUseCase {
    private final ImagePersistencePort imagePersistencePort;
    private final S3ManagePort s3ManagePort;

    @Override
    public void deleteImage(Long imageId) {
        Image image =
                imagePersistencePort
                        .findById(imageId)
                        .orElseThrow(() -> new NotFoundException(ImageErrorType.NOT_FOUND));

        s3ManagePort.deleteImageFromS3(image.getImageName());
        imagePersistencePort.deleteById(imageId);
    }

    @Override
    public void deleteAllImagesByMemoryId(Long memoryId) {
        List<Image> images = imagePersistencePort.findAllByMemoryId(memoryId);

        for (Image image : images) {
            s3ManagePort.deleteImageFromS3(image.getImageName());
        }
        imagePersistencePort.deleteAllByMemoryId(memoryId);
    }

    @Override
    public void deleteProfileImage(String profileImageUrl) {
        s3ManagePort.deleteImageFromS3(profileImageUrl);
    }
}
