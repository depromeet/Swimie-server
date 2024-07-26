package com.depromeet.image.service;

import com.depromeet.image.dto.response.ImageUploadResponseDto;
import com.depromeet.memory.Memory;
import java.util.List;

public interface ImageUpdateService {
    List<ImageUploadResponseDto> updateImages(Memory memory, List<String> imageNames);

    void changeImageStatus(List<Long> imageIds);
}
