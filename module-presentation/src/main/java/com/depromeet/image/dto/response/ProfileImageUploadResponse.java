package com.depromeet.image.dto.response;

import com.depromeet.image.domain.vo.ImagePresignedUrlNameVo;

public record ProfileImageUploadResponse(String imageName, String presignedUrl) {
    public static ProfileImageUploadResponse of(ImagePresignedUrlNameVo imagePresignedUrlNameVo) {
        return new ProfileImageUploadResponse(
                imagePresignedUrlNameVo.originImageName(), imagePresignedUrlNameVo.presignedUrl());
    }
}
