package com.depromeet.memory.mock;

import com.depromeet.image.port.out.s3.S3ManagePort;

public class FakeS3ImageManager implements S3ManagePort {
    @Override
    public String getPresignedUrl(String imageName) {
        return "http://presigned-url/" + imageName;
    }

    @Override
    public void deleteImageFromS3(String imageName) {}
}
