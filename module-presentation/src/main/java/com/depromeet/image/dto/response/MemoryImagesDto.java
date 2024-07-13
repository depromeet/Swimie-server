package com.depromeet.image.dto.response;

import lombok.Builder;

public record MemoryImagesDto(Long id, String imageName, String url) {
    @Builder
    public MemoryImagesDto {}
}
