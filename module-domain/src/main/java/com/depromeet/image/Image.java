package com.depromeet.image;

import com.depromeet.memory.Memory;
import java.util.Optional;
import lombok.Builder;

public class Image {
    private Long id;
    private Memory memory;
    private String imageName;
    private String imageUrl;

    @Builder
    public Image(Long id, Memory memory, String imageName, String imageUrl) {
        this.id = id;
        this.memory = memory;
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

    public String getImageName() {
        return this.imageName;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
