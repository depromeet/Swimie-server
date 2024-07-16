package com.depromeet.image.service;

import com.depromeet.memory.Memory;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    List<Long> uploadMemoryImages(List<MultipartFile> files);

    void addMemoryIdToImages(Memory memory, List<Long> imageIds);
}
