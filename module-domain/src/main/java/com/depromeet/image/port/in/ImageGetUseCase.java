package com.depromeet.image.port.in;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.vo.MemoryImageUrlVo;
import java.util.List;

public interface ImageGetUseCase {
    List<Image> findImagesByMemoryId(Long memoryId);

    List<MemoryImageUrlVo> findImagesByMemoryIds(List<Long> memoryIds);
}
