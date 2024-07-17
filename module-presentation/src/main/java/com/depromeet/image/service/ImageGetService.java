package com.depromeet.image.service;

import com.depromeet.image.dto.response.MemoryImagesDto;
import java.util.List;

public interface ImageGetService {
    List<MemoryImagesDto> findImagesByMemoryId(Long memoryId);
}
