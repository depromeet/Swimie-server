package com.depromeet.image.api;

import static com.depromeet.type.image.ImageSuccessType.*;

import com.depromeet.config.log.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.image.dto.request.ImageIdsRequest;
import com.depromeet.image.dto.request.ImageNameRequest;
import com.depromeet.image.dto.request.ProfileImageNameRequest;
import com.depromeet.image.dto.response.ImageResponse;
import com.depromeet.image.dto.response.ImageUploadResponse;
import com.depromeet.image.dto.response.ProfileImageUploadResponse;
import com.depromeet.image.facade.ImageFacade;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.type.image.ImageSuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController implements ImageApi {
    private final ImageFacade imageFacade;

    @PostMapping("/presigned-url")
    @Logging(item = "Image", action = "POST")
    public ApiResponse<?> getPresignedUrlForUploadImage(
            @RequestBody ImageNameRequest imageNameRequest) {
        List<ImageUploadResponse> imageUploadResponses =
                imageFacade.getPresignedUrlAndSaveImages(imageNameRequest);
        return ApiResponse.success(
                ImageSuccessType.GENERATE_PRESIGNED_URL_SUCCESS, imageUploadResponses);
    }

    @PutMapping("/profile/presigned-url")
    @Logging(item = "Image", action = "PUT")
    public ApiResponse<?> getPresignedUrlForUploadProfileImage(
            @LoginMember Long memberId,
            @RequestBody ProfileImageNameRequest profileImageNameRequest) {
        ProfileImageUploadResponse imageUploadResponse =
                imageFacade.getProflieImagePresignedUrlOrDeleteProfileImage(
                        memberId, profileImageNameRequest);
        if (imageUploadResponse == null) {
            return ApiResponse.success(DELETE_PROFILE_IMAGE_SUCCESS);
        }
        return ApiResponse.success(
                ImageSuccessType.GENERATE_PRESIGNED_URL_SUCCESS, imageUploadResponse);
    }

    @PatchMapping("/memory/{memoryId}")
    @Logging(item = "Image", action = "PATCH")
    public ApiResponse<?> updateImages(
            @PathVariable("memoryId") Long memoryId, @RequestBody ImageNameRequest imageNames) {
        List<ImageUploadResponse> images = imageFacade.updateImages(memoryId, imageNames);
        return ApiResponse.success(ImageSuccessType.UPDATE_AND_GET_PRESIGNED_URL_SUCCESS, images);
    }

    @PatchMapping("/status")
    @Logging(item = "Image", action = "PATCH")
    public ApiResponse<?> changeImageStatusForAddedImages(
            @RequestBody ImageIdsRequest imageIdsRequest) {
        imageFacade.changeImageStatus(imageIdsRequest.imageIds());
        return ApiResponse.success(ImageSuccessType.CHANGE_IMAGE_STATUS_SUCCESS);
    }

    @PatchMapping("/profile/url")
    @Logging(item = "Image", action = "PATCH")
    public ApiResponse<?> changeProfileImageUrl(
            @LoginMember Long memberId,
            @RequestBody ProfileImageNameRequest profileImageNameRequest) {
        imageFacade.changeProfileImageUrl(memberId, profileImageNameRequest.imageName());
        return ApiResponse.success(ImageSuccessType.CHANGE_PROFILE_IMAGE_URL_SUCCESS);
    }

    @GetMapping("/memory/{memoryId}")
    @Logging(item = "Image", action = "GET")
    public ApiResponse<List<ImageResponse>> findImages(@PathVariable("memoryId") Long memoryId) {
        List<ImageResponse> memoryImages = imageFacade.findImagesByMemoryId(memoryId);
        return ApiResponse.success(ImageSuccessType.GET_IMAGES_SUCCESS, memoryImages);
    }

    @DeleteMapping("/memory/{memoryId}")
    @Logging(item = "Image", action = "DELETE")
    public ApiResponse<?> deleteImages(@PathVariable("memoryId") Long memoryId) {
        imageFacade.deleteAllImagesByMemoryId(memoryId);
        return ApiResponse.success(ImageSuccessType.DELETE_IMAGE_SUCCESS);
    }
}
