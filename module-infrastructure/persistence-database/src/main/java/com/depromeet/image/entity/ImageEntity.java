package com.depromeet.image.entity;

import com.depromeet.image.Image;
import com.depromeet.memory.entity.MemoryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageEntity {
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "memory_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemoryEntity memory;

    @NotNull
    @Column(name = "image_name")
    private String imageName;

    @NotNull
    @Column(name = "image_url")
    private String imageUrl;

    @Builder
    public ImageEntity(Long id, MemoryEntity memory, String imageName, String imageUrl) {
        this.id = id;
        this.memory = memory;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public static ImageEntity from(Image image) {
        return ImageEntity.builder()
                .id(image.getId())
                .memory(
                        image.getMemory().isPresent()
                                ? MemoryEntity.from(image.getMemory().get())
                                : null)
                .imageName(image.getImageName())
                .imageUrl(image.getImageUrl())
                .build();
    }

    public Image toModel() {
        return Image.builder()
                .id(this.id)
                .memory(this.memory.toModel())
                .imageName(this.imageName)
                .imageUrl(this.imageUrl)
                .build();
    }

    public Long getId() {
        return id;
    }

    public MemoryEntity getMemory() {
        return memory;
    }

    public String getImageName() {
        return imageName;
    }

    public @NotNull String getImageUrl() {
        return imageUrl;
    }
}
