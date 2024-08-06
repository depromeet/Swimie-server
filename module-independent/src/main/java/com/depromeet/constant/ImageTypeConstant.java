package com.depromeet.constant;

import com.depromeet.exception.BadRequestException;
import com.depromeet.type.image.ImageErrorType;
import java.util.Arrays;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum ImageTypeConstant {
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),
    WEBP("webp", "image/webp");

    private final String extensionType;
    private final String contentType;

    ImageTypeConstant(String extensionType, String contentType) {
        this.extensionType = extensionType;
        this.contentType = contentType;
    }

    public static String fromExtensionType(String extensionType) {
        log.info("extensionType: {}", extensionType);
        if (extensionType == null) {
            throw new BadRequestException(ImageErrorType.IMAGE_TYPE_NULL);
        }

        return Arrays.stream(ImageTypeConstant.values())
                .filter(extension -> extension.extensionType.equals(extensionType))
                .findAny()
                .orElseThrow(() -> new BadRequestException(ImageErrorType.INVALID_IMAGE_TYPE))
                .getContentType();
    }
}
