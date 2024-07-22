package com.depromeet.image.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.image.dto.response.MemoryImagesDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지(images)")
public interface ImageApi {
    @Operation(summary = "수영 기록 이미지 업로드")
    ApiResponse<List<Long>> uploadImages(
            @RequestPart("images") @NotNull List<MultipartFile> images);

    @Operation(summary = "수영 기록 이미지 수정")
    ApiResponse<?> updateImages(
            @PathVariable("memoryId") Long memoryId,
            @RequestPart("images") List<MultipartFile> images);

    @Operation(summary = "수영 기록의 이미지 조회")
    ApiResponse<List<MemoryImagesDto>> findImages(@PathVariable("memoryId") Long memoryId);

    @Operation(summary = "Delete images belongs to memory", description = "수영 기록의 이미지 삭제")
    ApiResponse<?> deleteImages(@PathVariable("memoryId") Long memoryId);
}
