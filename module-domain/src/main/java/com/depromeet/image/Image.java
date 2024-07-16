package com.depromeet.image;

import com.depromeet.memory.Memory;
import java.util.Optional;
import lombok.Builder;

public class Image {
    private Long id;
    private Memory memory;
    private String originImageName;
    private String imageName;
    private String imageUrl;

    @Builder
    public Image(
            Long id, Memory memory, String originImageName, String imageName, String imageUrl) {
        this.id = id;
        this.memory = memory;
        this.originImageName = originImageName;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public void addMemoryToImage(Memory memory) {
        if (memory != null) {
            this.memory = memory;
        }
    }

    public Long getId() {
        return this.id;
    }

    public Optional<Memory> getMemory() {
        return Optional.ofNullable(this.memory);
    }

    public String getOriginImageName() {
        return originImageName;
    }

    public String getImageName() {
        return this.imageName;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
