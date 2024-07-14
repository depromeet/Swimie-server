package com.depromeet.image.service;

public interface ImageDeleteService {
    void deleteImage(Long imageId);

    void deleteAllImagesByMemoryId(Long memoryId);
}
