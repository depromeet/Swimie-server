package com.depromeet.image.port.in;

public interface ImageDeleteUseCase {
    void deleteImage(Long imageId);

    void deleteAllImagesByMemoryId(Long memoryId);
}
