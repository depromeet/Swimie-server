package com.depromeet.image.api;

import com.depromeet.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지(images)")
public interface ImageApi {
    @Operation(summary = "수영 기록 이미지 업로드")
    ApiResponse<?> uploadImages(@RequestPart @NotNull List<MultipartFile> images);

    @Operation(summary = "수영 기록 이미지 수정")
    ApiResponse<?> updateImages(
            @RequestParam("memoryId") Long memoryId, @RequestPart List<MultipartFile> images);

    @Operation(summary = "수영 기록의 이미지 조회")
    ApiResponse<?> findImages(@RequestParam(name = "memoryId") Long memoryId);

    @Operation(summary = "Delete images belongs to memory", description = "수영 기록의 이미지 삭제")
    ResponseEntity<?> deleteImages(@RequestParam(value = "memoryId") Long memoryId);
}
