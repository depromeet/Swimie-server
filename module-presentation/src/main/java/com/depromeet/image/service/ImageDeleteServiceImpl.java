package com.depromeet.image.service;

import com.depromeet.exception.NotFoundException;
import com.depromeet.image.Image;
import com.depromeet.image.repository.ImageRepository;
import com.depromeet.type.image.ImageErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageDeleteServiceImpl implements ImageDeleteService {
    private final ImageRepository imageRepository;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public void deleteImage(Long imageId) {
        Image image =
                imageRepository
                        .findById(imageId)
                        .orElseThrow(() -> new NotFoundException(ImageErrorType.NOT_FOUND));

        deleteImageFromS3(image);
        imageRepository.deleteById(imageId);
    }

    @Override
    public void deleteAllImagesByMemoryId(Long memoryId) {
        List<Image> images = imageRepository.findImagesByMemoryId(memoryId);

        for (Image image : images) {
            deleteImageFromS3(image);
        }
        imageRepository.deleteAllByMemoryId(memoryId);
    }

    private void deleteImageFromS3(Image image) {
        DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder().bucket(bucketName).key(image.getImageName()).build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
