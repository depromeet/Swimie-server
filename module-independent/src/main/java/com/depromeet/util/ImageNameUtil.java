package com.depromeet.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ImageNameUtil {
    public static String createImageName(String originalImageName, LocalDateTime localDateTime) {
        int extensionIdx = originalImageName.lastIndexOf(".") + 1;
        String extension = originalImageName.substring(extensionIdx).toLowerCase();

        String localDateTimeFormat =
                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));

        String imageName = originalImageName + localDateTimeFormat;

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
