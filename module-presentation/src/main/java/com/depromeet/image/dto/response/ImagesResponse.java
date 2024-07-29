package com.depromeet.image.dto.response;

import lombok.Builder;

public record ImagesResponse(Long imageId, String originImageName, String imageName, String url) {
    @Builder
    public ImagesResponse {}
}
