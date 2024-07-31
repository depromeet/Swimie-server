package com.depromeet.image.dto.response;

import com.depromeet.image.domain.Image;
import lombok.Builder;

public record ImageSimpleResponse(String imageName, String url) {
    @Builder
    public ImageSimpleResponse {}

    public static ImageSimpleResponse of(Image image) {
        return ImageSimpleResponse.builder()
                .imageName(image.getImageName())
                .url(image.getImageUrl())
                .build();
    }
}
