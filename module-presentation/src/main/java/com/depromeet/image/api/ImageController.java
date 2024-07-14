package com.depromeet.image.api;

import static com.depromeet.type.image.ImageSuccessType.DELETE_IMAGES_SUCCESS;
import static com.depromeet.type.image.ImageSuccessType.UPLOAD_IMAGES_SUCCESS;

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
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "수영 기록 이미지 s3에 업로드")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> uploadImages(@RequestPart List<MultipartFile> images) {
        List<Long> imageIds = imageUploadService.uploadMemoryImages(images);

        return ResponseEntity.ok(ApiResponse.success(UPLOAD_IMAGES_SUCCESS, imageIds));
    }

    @Operation(summary = "업로드 된 이미지에 memoryId 추가")
    @PatchMapping("/memory") // 임시로 작성하긴 했는데 직관적이지 않네요 api 작명 추천 받습니다.
    public ResponseEntity<ApiResponse<?>> addMemoryToImages(
            @RequestParam(name = "memoryId") Long memoryId,
            @RequestBody ImagesMemoryIdDto imagesMemoryIdDto) {
        imageUploadService.addMemoryIdToImages(memoryId, imagesMemoryIdDto);

        return ResponseEntity.ok(ApiResponse.success(UPLOAD_IMAGES_SUCCESS));
    }

    @Operation(summary = "수영 기록의 이미지 수정")
    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateImages(
            @RequestParam(name = "memoryId") Long memoryId,
            @RequestPart List<MultipartFile> images) {
        imageUpdateService.updateImages(memoryId, images);

        return ResponseEntity.ok(ApiResponse.success(UPLOAD_IMAGES_SUCCESS));
    }

    @Operation(summary = "수영 기록의 이미지 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> findImages(
            @RequestParam(name = "memoryId") Long memoryId) {
        List<MemoryImagesDto> memoryImages = imageGetService.findImagesByMemoryId(memoryId);

        return ResponseEntity.ok(ApiResponse.success(UPLOAD_IMAGES_SUCCESS, memoryImages));
    }

    @Operation(summary = "Delete images belongs to memory", description = "수영 기록의 이미지 삭제")
    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteImages(
            @RequestParam(value = "memoryId") Long memoryId) {
        imageDeleteService.deleteAllImagesByMemoryId(memoryId);

        return ResponseEntity.ok(ApiResponse.success(DELETE_IMAGES_SUCCESS));
    }
}
