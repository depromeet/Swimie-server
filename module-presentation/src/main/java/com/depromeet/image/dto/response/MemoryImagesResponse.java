package com.depromeet.image.dto.response;

import lombok.Builder;

public record MemoryImagesResponse(
        Long imageId, String originImageName, String imageName, String url) {
    @Builder
    public MemoryImagesResponse {}
}
