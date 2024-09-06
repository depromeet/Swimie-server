package com.depromeet.image.port.in;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.vo.MemoryImageUrlVo;
import java.util.List;
import java.util.Map;

public interface ImageGetUseCase {
    List<Image> findImagesByMemoryId(Long memoryId);

    Map<Long, MemoryImageUrlVo> findImagesByMemoryIds(List<Long> memoryIds);
}
