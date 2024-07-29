package com.depromeet.memory.fixture.dto;

import com.depromeet.image.dto.response.ImageUploadResponseDto;
import java.util.ArrayList;
import java.util.List;

public class ImageUploadResponseDtoFixture {
    public static List<ImageUploadResponseDto> make(List<String> imageNames) {
        List<ImageUploadResponseDto> images = new ArrayList<>();
        for (int i = 0; i < imageNames.size(); i++) {
            ImageUploadResponseDto imageUploadResponseDto =
                    ImageUploadResponseDto.builder()
                            .imageId((long) i + 1)
                            .imageName(imageNames.get(i))
                            .presignedUrl("http://" + imageNames.get(i))
                            .build();
            images.add(imageUploadResponseDto);
        }
        return images;
    }
}
