package com.depromeet.image.dto.response;

import lombok.Builder;

public record MemoryImagesDto(Long id, String originImageName, String imageName, String url) {
    @Builder
    public MemoryImagesDto {}
}
