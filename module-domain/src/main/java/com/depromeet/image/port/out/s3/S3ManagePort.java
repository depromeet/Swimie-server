package com.depromeet.image.port.out.s3;

public interface S3ManagePort {
    String getPresignedUrl(String imageName, String contentType);

    void deleteImageFromS3(String imageName);
}
