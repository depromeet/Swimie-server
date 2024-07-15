package com.depromeet.image.api;

import static com.depromeet.type.image.ImageSuccessType.*;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.image.dto.request.ImagesMemoryIdDto;
import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.image.service.ImageDeleteService;
import com.depromeet.image.service.ImageGetService;
import com.depromeet.image.service.ImageUpdateService;
import com.depromeet.image.service.ImageUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지(images)")
@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageUploadService imageUploadService;
    private final ImageUpdateService imageUpdateService;
    private final ImageGetService imageGetService;
    private final ImageDeleteService imageDeleteService;

    @PostMapping
    @Operation(summary = "수영 기록 이미지 s3에 업로드")
    public ApiResponse<?> uploadImages(@RequestPart List<MultipartFile> images) {
        List<Long> imageIds = imageUploadService.uploadMemoryImages(images);

        return ApiResponse.success(UPLOAD_IMAGES_SUCCESS, imageIds);
    }

    @PatchMapping("/memory") // 임시로 작성하긴 했는데 직관적이지 않네요 api 작명 추천 받습니다.
    @Operation(summary = "업로드 된 이미지에 memoryId 추가")
    public ApiResponse<?> addMemoryToImages(
            @RequestParam(name = "memoryId") Long memoryId,
            @RequestBody ImagesMemoryIdDto imagesMemoryIdDto) {
        imageUploadService.addMemoryIdToImages(memoryId, imagesMemoryIdDto);

        return ApiResponse.success(ADD_MEMORY_TO_IMAGES_SUCCESS);
    }

    @PatchMapping
    @Operation(summary = "수영 기록의 이미지 수정")
    public ApiResponse<?> updateImages(
            @RequestParam(name = "memoryId") Long memoryId,
            @RequestPart List<MultipartFile> images) {
        imageUpdateService.updateImages(memoryId, images);

        return ApiResponse.success(UPDATE_IMAGES_SUCCESS);
    }

    @GetMapping
    @Operation(summary = "수영 기록의 이미지 조회")
    public ApiResponse<?> findImages(@RequestParam(name = "memoryId") Long memoryId) {
        List<MemoryImagesDto> memoryImages = imageGetService.findImagesByMemoryId(memoryId);

        return ApiResponse.success(GET_IMAGES_SUCCESS, memoryImages);
    }

    @DeleteMapping
    @Operation(summary = "Delete images belongs to memory", description = "수영 기록의 이미지 삭제")
    public ApiResponse<?> deleteImages(@RequestParam(value = "memoryId") Long memoryId) {
        imageDeleteService.deleteAllImagesByMemoryId(memoryId);

        return ApiResponse.success(DELETE_IMAGES_SUCCESS);
    }
}
