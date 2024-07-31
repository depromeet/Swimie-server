package com.depromeet.fixture;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.memory.domain.Memory;
import com.depromeet.util.ImageNameUtil;
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
            String uuidImageName = ImageNameUtil.createImageName(originImageNames.get(i));

            Image image =
                    Image.builder()
                            .memory(memory)
                            .originImageName(originImageNames.get(i))
                            .imageName(uuidImageName)
                            .imageUploadStatus(status)
                            .imageUrl("http://" + uuidImageName)
                            .build();
            images.add(image);
        }
        return images;
    }
}
