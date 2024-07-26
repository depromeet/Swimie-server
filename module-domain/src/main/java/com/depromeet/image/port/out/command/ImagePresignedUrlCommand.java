package com.depromeet.image.port.out.command;

import lombok.Builder;

public record ImagePresignedUrlCommand(Long imageId, String imageName, String presignedUrl) {
    @Builder
    public ImagePresignedUrlCommand {}
}
