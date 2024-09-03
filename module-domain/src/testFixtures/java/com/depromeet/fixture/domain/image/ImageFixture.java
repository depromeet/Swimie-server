package com.depromeet.fixture.domain.image;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.memory.domain.Memory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static List<Image> makeImages(Memory memory) {
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

    public static List<Image> makeImages(
            List<String> originImageNames,
            Memory memory,
            ImageUploadStatus status,
            LocalDateTime localDateTime) {
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < originImageNames.size(); i++) {
            String uuidImageName = createImageName(originImageNames.get(i), localDateTime);

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

    public static String createImageName(String originImageName, LocalDateTime localDateTime) {
        int extensionIdx = originImageName.lastIndexOf(".") + 1;
        String extension = originImageName.substring(extensionIdx).toLowerCase();

        String localDateTimeFormat =
                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));

        String imageName = originImageName + localDateTimeFormat;

        UUID imageUUID = UUID.nameUUIDFromBytes(imageName.getBytes());

        return imageUUID + "." + extension;
    }
}
