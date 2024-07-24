package com.depromeet.image;

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
    private boolean isPending;

    @Builder
    public Image(
            Long id,
            Memory memory,
            String originImageName,
            String imageName,
            String imageUrl,
            boolean isPending) {
        this.id = id;
        this.memory = memory;
        this.originImageName = originImageName;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.isPending = isPending;
    }

    public void addMemoryToImage(Memory memory) {
        if (memory != null) {
            this.memory = memory;
        }
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
                .isPending(isPending)
                .build();
    }
}
