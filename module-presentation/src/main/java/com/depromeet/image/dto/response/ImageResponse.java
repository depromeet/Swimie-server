package com.depromeet.image.dto.response;

import com.depromeet.image.domain.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record ImageResponse(
        @Schema(description = "image PK", example = "1") Long imageId,
        @Schema(description = "원래 이미지 이름", example = "image.png") String originImageName,
        @Schema(
                        description = "uuid로 변환된 이미지 이름",
                        example = "8d1e7137-69d5-37db-ae65-870145893168.png")
                String imageName,
        @Schema(
                        description = "이미지 url",
                        example = "https://image-url/8d1e7137-69d5-37db-ae65-870145893168.png")
                String url) {
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
