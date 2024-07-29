package com.depromeet.image.api;

import static com.depromeet.type.image.ImageSuccessType.*;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.image.dto.request.ImageIdsDto;
import com.depromeet.image.dto.request.ImageNameDto;
import com.depromeet.image.dto.response.ImageUploadResponseDto;
import com.depromeet.image.dto.response.MemoryImagesDto;
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
    public ApiResponse<?> getPresignedUrlForUploadImage(@RequestBody ImageNameDto imageNameDto) {
        List<ImageUploadResponseDto> imageUploadResponseDtos =
                imageFacade.getPresignedUrlAndSaveImages(imageNameDto.imageNames());

        return ApiResponse.success(GENERATE_PRESIGNED_URL_SUCCESS, imageUploadResponseDtos);
    }

    @PatchMapping("/memory/{memoryId}")
    public ApiResponse<?> updateImages(
            @PathVariable("memoryId") Long memoryId, @RequestBody ImageNameDto imageNames) {
        List<ImageUploadResponseDto> images =
                imageFacade.updateImages(memoryId, imageNames.imageNames());

        return ApiResponse.success(UPDATE_AND_GET_PRESIGNED_URL_SUCCESS, images);
    }

    @PatchMapping("/status")
    public ApiResponse<?> changeImageStatusForAddedImages(@RequestBody ImageIdsDto imageIdsDto) {
        imageFacade.changeImageStatus(imageIdsDto.imageIds());

        return ApiResponse.success(CHANGE_IMAGE_STATUS_SUCCESS);
    }

    @GetMapping("/memory/{memoryId}")
    public ApiResponse<List<MemoryImagesDto>> findImages(@PathVariable("memoryId") Long memoryId) {
        List<MemoryImagesDto> memoryImages = imageFacade.findImagesByMemoryId(memoryId);

        return ApiResponse.success(GET_IMAGES_SUCCESS, memoryImages);
    }

    @DeleteMapping("/memory/{memoryId}")
    public ResponseEntity<?> deleteImages(@PathVariable("memoryId") Long memoryId) {
        imageFacade.deleteAllImagesByMemoryId(memoryId);

        return ResponseEntity.noContent().build();
    }
}
