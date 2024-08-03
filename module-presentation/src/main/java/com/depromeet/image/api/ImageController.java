package com.depromeet.image.api;

import static com.depromeet.type.image.ImageSuccessType.*;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.image.dto.request.ImageIdsRequest;
import com.depromeet.image.dto.request.ImageNameRequest;
import com.depromeet.image.dto.response.ImageResponse;
import com.depromeet.image.dto.response.ImageUploadResponse;
import com.depromeet.image.facade.ImageFacade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

        return ApiResponse.success(GENERATE_PRESIGNED_URL_SUCCESS, imageUploadResponses);
    }

    @PatchMapping("/memory/{memoryId}")
    @Logging(item = "Image", action = "PATCH")
    public ApiResponse<?> updateImages(
            @PathVariable("memoryId") Long memoryId, @RequestBody ImageNameRequest imageNames) {
        List<ImageUploadResponse> images = imageFacade.updateImages(memoryId, imageNames);

        return ApiResponse.success(UPDATE_AND_GET_PRESIGNED_URL_SUCCESS, images);
    }

    @PatchMapping("/status")
    @Logging(item = "Image", action = "PATCH")
    public ApiResponse<?> changeImageStatusForAddedImages(
            @RequestBody ImageIdsRequest imageIdsRequest) {
        imageFacade.changeImageStatus(imageIdsRequest.imageIds());

        return ApiResponse.success(CHANGE_IMAGE_STATUS_SUCCESS);
    }

    @GetMapping("/memory/{memoryId}")
    @Logging(item = "Image", action = "GET")
    public ApiResponse<List<ImageResponse>> findImages(@PathVariable("memoryId") Long memoryId) {
        List<ImageResponse> memoryImages = imageFacade.findImagesByMemoryId(memoryId);

        return ApiResponse.success(GET_IMAGES_SUCCESS, memoryImages);
    }

    @DeleteMapping("/memory/{memoryId}")
    @Logging(item = "Image", action = "DELETE")
    public ResponseEntity<?> deleteImages(@PathVariable("memoryId") Long memoryId) {
        imageFacade.deleteAllImagesByMemoryId(memoryId);

        return ResponseEntity.noContent().build();
    }
}
