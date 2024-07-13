package com.depromeet.util;

import java.util.UUID;

public class ImageNameUtil {
    public static String createImageName(
            String originalImageName, String contentType, Long fileSize) {
        String imageName = originalImageName + "_" + contentType + "_" + fileSize;
        UUID imageUUID = UUID.nameUUIDFromBytes(imageName.getBytes());

        return imageUUID.toString();
    }
}
