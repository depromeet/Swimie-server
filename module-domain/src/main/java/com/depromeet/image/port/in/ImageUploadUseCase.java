package com.depromeet.image.port.in;

import com.depromeet.image.domain.vo.ImagePresignedUrlNameVo;
import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.depromeet.memory.domain.Memory;
import java.util.List;

public interface ImageUploadUseCase {
    List<ImagePresignedUrlVo> getPresignedUrlAndSaveImages(List<String> originImageNames);

    ImagePresignedUrlNameVo getPresignedUrlAndSaveProfileImage(String originImageName);

    void changeImageStatusAndAddMemoryIdToImages(Memory memory, List<Long> imageIds);
}
