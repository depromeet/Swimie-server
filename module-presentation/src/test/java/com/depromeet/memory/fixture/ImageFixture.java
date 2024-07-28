package com.depromeet.memory.fixture;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.memory.Memory;
import java.util.ArrayList;
import java.util.List;

public class ImageFixture {
    public static Image make(String originImageNames, Memory memory, ImageUploadStatus status) {
        return Image.builder()
                .memory(memory)
                .originImageName(originImageNames)
                .imageName("imageName.png")
                .imageUploadStatus(status)
                .imageUrl("http://imageName.png")
                .build();
    }

    public static List<Image> makeImages(
            List<String> originImageNames, Memory memory, ImageUploadStatus status) {
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < originImageNames.size(); i++) {
            Image image =
                    Image.builder()
                            .memory(memory)
                            .originImageName(originImageNames.get(i))
                            .imageName("imageName" + i + 1 + ".png")
                            .imageUploadStatus(status)
                            .imageUrl("http://imageName" + i + 1 + ".png")
                            .build();
            images.add(image);
        }
        return images;
    }
}
