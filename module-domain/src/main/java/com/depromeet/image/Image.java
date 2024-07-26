package com.depromeet.image;

import com.depromeet.memory.ImageUploadStatus;
import com.depromeet.memory.Memory;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Image {
    private Long id;
    private Memory memory;
    private String originImageName;
    private String imageName;
    private String imageUrl;
    private ImageUploadStatus imageUploadStatus;

    @Builder
    public Image(
            Long id,
            Memory memory,
            String originImageName,
            String imageName,
            String imageUrl,
            ImageUploadStatus imageUploadStatus) {
        this.id = id;
        this.memory = memory;
        this.originImageName = originImageName;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.imageUploadStatus = imageUploadStatus;
    }

    public void addMemoryToImage(Memory memory) {
        if (memory != null) {
            this.memory = memory;
        }
    }

    public void updateHasUploaded() {
        this.imageUploadStatus = ImageUploadStatus.UPLOADED;
    }

    public Optional<Memory> getMemory() {
        return Optional.ofNullable(this.memory);
    }

    public Image withoutMemory() {
        return Image.builder()
                .id(id)
                .originImageName(originImageName)
                .imageName(imageName)
                .imageUrl(imageUrl)
                .imageUploadStatus(imageUploadStatus)
                .build();
    }
}
