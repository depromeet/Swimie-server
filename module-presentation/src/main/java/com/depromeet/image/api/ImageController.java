package com.depromeet.image.api;

import static com.depromeet.type.image.ImageSuccessType.*;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.image.service.ImageDeleteService;
import com.depromeet.image.service.ImageGetService;
import com.depromeet.image.service.ImageUpdateService;
import com.depromeet.image.service.ImageUploadService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController implements ImageApi {
    private final ImageUploadService imageUploadService;
    private final ImageUpdateService imageUpdateService;
    private final ImageGetService imageGetService;
    private final ImageDeleteService imageDeleteService;

    @PostMapping
    public ApiResponse<?> uploadImages(@RequestPart @NotNull List<MultipartFile> images) {
        List<Long> imageIds = imageUploadService.uploadMemoryImages(images);

        return ApiResponse.success(UPLOAD_IMAGES_SUCCESS, imageIds);
    }

    @PatchMapping("/memory/{memoryId}")
    public ApiResponse<?> updateImages(
            @RequestParam("memoryId") Long memoryId, @RequestPart List<MultipartFile> images) {
        imageUpdateService.updateImages(memoryId, images);

        return ApiResponse.success(UPDATE_IMAGES_SUCCESS);
    }

    @GetMapping
    public ApiResponse<?> findImages(@RequestParam(name = "memoryId") Long memoryId) {
        List<MemoryImagesDto> memoryImages = imageGetService.findImagesByMemoryId(memoryId);

        return ApiResponse.success(GET_IMAGES_SUCCESS, memoryImages);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteImages(@RequestParam(value = "memoryId") Long memoryId) {
        imageDeleteService.deleteAllImagesByMemoryId(memoryId);

        return ResponseEntity.noContent().build();
    }
}
