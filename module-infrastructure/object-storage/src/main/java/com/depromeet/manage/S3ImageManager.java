package com.depromeet.manage;

import com.depromeet.image.port.out.s3.S3ManagePort;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class S3ImageManager implements S3ManagePort {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String getPresignedUrl(String imageName) {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(imageName)
                        .contentType("image/webp")
                        .build();

        PutObjectPresignRequest putObjectPresignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(5))
                        .putObjectRequest(putObjectRequest)
                        .build();

        PresignedPutObjectRequest presignedPutObjectRequest =
                s3Presigner.presignPutObject(putObjectPresignRequest);

        return presignedPutObjectRequest.url().toString();
    }

    @Override
    public void deleteImageFromS3(String imageName) {
        DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder().bucket(bucketName).key(imageName).build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
