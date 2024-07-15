package com.depromeet.util;

import java.util.UUID;

public class ImageNameUtil {
    public static String createImageName(String originalImageName) {

        int extractExtension = originalImageName.lastIndexOf(".") + 1;
        String extension = originalImageName.substring(extractExtension).toLowerCase();

        UUID imageUUID = UUID.nameUUIDFromBytes(originalImageName.getBytes());

        return imageUUID + "." + extension;
    }
}
