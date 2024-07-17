package com.depromeet.image.service;

import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.image.repository.ImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageGetServiceImpl implements ImageGetService {
    private final ImageRepository imageRepository;

    @Value("${cloud-front.domain}")
    private String domain;

    @Override
    public List<MemoryImagesDto> findImagesByMemoryId(Long memoryId) {
        return imageRepository.findImagesByMemoryId(memoryId).stream()
                .map(
                        image ->
                                MemoryImagesDto.builder()
                                        .id(image.getId())
                                        .originImageName(image.getOriginImageName())
                                        .imageName(image.getImageName())
                                        .url(domain + "/" + image.getImageName())
                                        .build())
                .toList();
    }
}
