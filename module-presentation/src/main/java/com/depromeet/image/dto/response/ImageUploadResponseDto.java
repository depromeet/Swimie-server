package com.depromeet.image.dto.response;

import com.depromeet.image.port.out.command.ImagePresignedUrlCommand;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageUploadResponseDto(Long imageId, String imageName, String presignedUrl) {
    @Builder
    public ImageUploadResponseDto {}

    public static ImageUploadResponseDto of(ImagePresignedUrlCommand imagePresignedUrlCommand) {
        return ImageUploadResponseDto.builder()
                .imageId(imagePresignedUrlCommand.imageId())
                .imageName(imagePresignedUrlCommand.imageName())
                .presignedUrl(imagePresignedUrlCommand.presignedUrl())
                .build();
    }
}
