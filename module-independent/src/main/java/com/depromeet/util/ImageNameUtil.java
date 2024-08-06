package com.depromeet.util;

import com.depromeet.constant.ImageTypeConstant;
import com.depromeet.exception.BadRequestException;
import com.depromeet.type.image.ImageErrorType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ImageNameUtil {
    public static String createImageName(String originImageName, LocalDateTime localDateTime) {
        if (originImageName == null || originImageName.equals("")) {
            throw new BadRequestException(ImageErrorType.INVALID_IMAGE_NAME);
        }

        int extensionIdx = originImageName.lastIndexOf(".") + 1;
        String extension = originImageName.substring(extensionIdx).toLowerCase();

        String localDateTimeFormat =
                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));

        String imageName = originImageName + localDateTimeFormat;

        UUID imageUUID = UUID.nameUUIDFromBytes(imageName.getBytes());

        return imageUUID + "." + extension;
    }

    public static String getContentType(String originalImageName) {
        int extensionIdx = originalImageName.lastIndexOf(".") + 1;
        String extension = originalImageName.substring(extensionIdx).toLowerCase();

        return ImageTypeConstant.fromExtensionType(extension);
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
