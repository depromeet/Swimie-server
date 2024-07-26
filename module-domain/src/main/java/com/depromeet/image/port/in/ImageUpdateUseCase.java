package com.depromeet.image.port.in;

import com.depromeet.image.port.out.command.ImagePresignedUrlCommand;
import com.depromeet.memory.Memory;
import java.util.List;

public interface ImageUpdateUseCase {
    List<ImagePresignedUrlCommand> updateImages(Memory memory, List<String> imageNames);

    void changeImageStatus(List<Long> imageIds);
}
