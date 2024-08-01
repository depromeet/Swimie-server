package com.depromeet.manage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Component
@Profile("batch")
@RequiredArgsConstructor
public class BatchS3ImageManager {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public void deleteImageFromS3(String imageName) {
        DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder().bucket(bucketName).key(imageName).build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
