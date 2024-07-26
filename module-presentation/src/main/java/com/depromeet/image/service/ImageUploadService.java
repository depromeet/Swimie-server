package com.depromeet.image.service;

import com.depromeet.image.dto.response.ImageUploadResponseDto;
import com.depromeet.memory.Memory;
import java.util.List;

public interface ImageUploadService {
    List<ImageUploadResponseDto> getPresignedUrlAndSaveImages(List<String> originImageNames);

    void changeImageStatusAndAddMemoryIdToImages(Memory memory, List<Long> imageIds);
}
