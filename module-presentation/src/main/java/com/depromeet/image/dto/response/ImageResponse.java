package com.depromeet.image.dto.response;

import com.depromeet.image.domain.Image;
import lombok.Builder;

public record ImageResponse(Long imageId, String originImageName, String imageName, String url) {
    @Builder
    public ImageResponse {}

    public static ImageResponse of(Image image) {
        return ImageResponse.builder()
                .imageId(image.getId())
                .originImageName(image.getOriginImageName())
                .imageName(image.getImageName())
                .url(image.getImageUrl())
                .build();
    }
}
