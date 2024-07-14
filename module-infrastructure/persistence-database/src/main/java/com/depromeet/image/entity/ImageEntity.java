package com.depromeet.image.entity;

import com.depromeet.image.Image;
import com.depromeet.memory.entity.MemoryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
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

    @NotNull private String imageName;

    @NotNull private String imageUrl;

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
                .memory(this.memory == null ? null : this.memory.toModel())
                .imageName(this.imageName)
                .imageUrl(this.imageUrl)
                .build();
    }

    public Long getId() {
        return this.id;
    }

    public Optional<MemoryEntity> getMemory() {
        return Optional.ofNullable(this.memory);
    }

    public String getImageName() {
        return this.imageName;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
