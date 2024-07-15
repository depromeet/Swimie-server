package com.depromeet.util;

import java.util.UUID;

public class ImageNameUtil {
    public static String createImageName(String originalImageName) {

        int extensionIdx = originalImageName.lastIndexOf(".") + 1;
        String extension = originalImageName.substring(extensionIdx).toLowerCase();

        UUID imageUUID = UUID.nameUUIDFromBytes(originalImageName.getBytes());

        return imageUUID + "." + extension;
    }
}
