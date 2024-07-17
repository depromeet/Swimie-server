package com.depromeet.image.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUpdateService {
    void updateImages(Long memoryId, List<MultipartFile> images);
}
