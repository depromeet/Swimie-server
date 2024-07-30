package com.depromeet.memory.fixture.dto;

import com.depromeet.image.dto.response.ImageUploadResponse;
import java.util.ArrayList;
import java.util.List;

public class ImageUploadResponseDtoFixture {
    public static List<ImageUploadResponse> make(List<String> imageNames) {
        List<ImageUploadResponse> images = new ArrayList<>();
        for (int i = 0; i < imageNames.size(); i++) {
            ImageUploadResponse imageUploadResponse =
                    ImageUploadResponse.builder()
                            .imageId((long) i + 1)
                            .imageName(imageNames.get(i))
                            .presignedUrl("http://" + imageNames.get(i))
                            .build();
            images.add(imageUploadResponse);
        }
        return images;
    }
}
