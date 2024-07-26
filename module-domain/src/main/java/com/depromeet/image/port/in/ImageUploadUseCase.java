package com.depromeet.image.port.in;

import com.depromeet.image.port.out.command.ImagePresignedUrlCommand;
import com.depromeet.memory.Memory;
import java.util.List;

public interface ImageUploadUseCase {
    List<ImagePresignedUrlCommand> getPresignedUrlAndSaveImages(List<String> originImageNames);

    void changeImageStatusAndAddMemoryIdToImages(Memory memory, List<Long> imageIds);
}
