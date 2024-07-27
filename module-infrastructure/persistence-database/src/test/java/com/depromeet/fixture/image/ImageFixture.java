package com.depromeet.fixture.image;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.memory.Memory;
import java.util.ArrayList;
import java.util.List;

public class ImageFixture {
    public static Image imageFixture(Memory memory) {
        return Image.builder()
                .memory(memory)
                .originImageName("originImageName.png")
                .imageName("imageName.png")
                .imageUploadStatus(ImageUploadStatus.PENDING)
                .imageUrl("http://imageName.png")
                .build();
    }

    public static List<Image> imageListFixture(Memory memory) {
        List<Image> images = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Image image =
                    Image.builder()
                            .memory(memory)
                            .originImageName("originImageName" + i + ".png")
                            .imageName("imageName" + i + ".png")
                            .imageUploadStatus(ImageUploadStatus.PENDING)
                            .imageUrl("http://imageName" + i + ".png")
                            .build();
            images.add(image);
        }
        return images;
    }
}
