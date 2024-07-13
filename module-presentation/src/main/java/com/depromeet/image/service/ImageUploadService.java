package com.depromeet.image.service;

import com.depromeet.image.dto.request.ImagesMemoryIdDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    List<Long> uploadMemoryImages(List<MultipartFile> files);

    void addMemoryIdToImages(ImagesMemoryIdDto inputImagesMemoryIdDto);
}
