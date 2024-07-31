package com.depromeet.util;

import java.util.UUID;

public class ImageNameUtil {
    public static String createImageName(String originalImageName) {

        int extensionIdx = originalImageName.lastIndexOf(".") + 1;
        String extension = originalImageName.substring(extensionIdx).toLowerCase();
        String imageName = originalImageName + System.currentTimeMillis();

        UUID imageUUID = UUID.nameUUIDFromBytes(imageName.getBytes());

        return imageUUID + "." + extension;
    }

    public static boolean validateImageNameIsUUID(String imageName) {
        String[] imageNameArr = imageName.split("\\.");
        String pureImageName = imageNameArr[0];

        try {
            UUID.fromString(pureImageName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
