package com.depromeet.image.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.image.dto.request.ImageIdsDto;
import com.depromeet.image.dto.request.ImageNameDto;
import com.depromeet.image.dto.response.MemoryImagesDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "이미지(images)")
public interface ImageApi {
    @Operation(summary = "이미지 업로드 PresignedURL 생성")
    ApiResponse<?> getPresignedUrlForUploadImage(@RequestBody ImageNameDto imageNameDto);

    @Operation(summary = "수영 기록 이미지 수정")
    ApiResponse<?> updateImages(
            @PathVariable("memoryId") Long memoryId, @RequestBody ImageNameDto imageNames);

    @Operation(summary = "수영 기록 이미지 수정시 추가된 이미지가 잘 올라갔는지 확정")
    ApiResponse<?> changeImageStatusForAddedImages(@RequestBody ImageIdsDto imageIdsDto);

    @Operation(summary = "수영 기록의 이미지 조회")
    ApiResponse<List<MemoryImagesDto>> findImages(@PathVariable("memoryId") Long memoryId);

    @Operation(summary = "Delete images belongs to memory", description = "수영 기록의 이미지 삭제")
    ResponseEntity<?> deleteImages(@PathVariable("memoryId") Long memoryId);
}
