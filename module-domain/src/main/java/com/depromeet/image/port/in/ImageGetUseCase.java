package com.depromeet.image.port.in;

import com.depromeet.image.domain.Image;
import java.util.List;

public interface ImageGetUseCase {
    List<Image> findImagesByMemoryId(Long memoryId);
}
