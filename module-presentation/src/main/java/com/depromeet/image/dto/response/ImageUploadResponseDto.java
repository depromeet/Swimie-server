package com.depromeet.image.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageUploadResponseDto(Long imageId, String imageName, String presignedUrl) {
    @Builder
    public ImageUploadResponseDto {}
}
