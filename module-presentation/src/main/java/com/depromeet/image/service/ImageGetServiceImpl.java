package com.depromeet.image.service;

import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.image.repository.ImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageGetServiceImpl implements ImageGetService {
    private final ImageRepository imageRepository;

    @Override
    public List<MemoryImagesDto> findImagesByMemoryId(Long memoryId) {
        return imageRepository.findImagesByMemoryId(memoryId).stream()
                .map(
                        image ->
                                MemoryImagesDto.builder()
                                        .id(image.getId())
                                        .imageName(image.getImageName())
                                        .url(image.getImageUrl())
                                        .build())
                .toList();
    }
}
