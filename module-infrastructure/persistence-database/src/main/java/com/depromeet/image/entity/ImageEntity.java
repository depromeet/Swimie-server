package com.depromeet.image.entity;

import com.depromeet.image.Image;
import com.depromeet.memory.entity.MemoryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    @NotNull private String originImageName;

    @NotNull private String imageName;

    @NotNull private String imageUrl;

    private boolean isPending;

    @Builder
    public ImageEntity(
            Long id,
            MemoryEntity memory,
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

    public static ImageEntity from(Image image) {
        return ImageEntity.builder()
                .id(image.getId())
                .memory(
                        image.getMemory().isPresent()
                                ? MemoryEntity.from(image.getMemory().get())
                                : null)
                .originImageName(image.getOriginImageName())
                .imageName(image.getImageName())
                .imageUrl(image.getImageUrl())
                .isPending(image.isPending())
                .build();
    }

    public static ImageEntity pureFrom(Image image) {
        return ImageEntity.builder()
                .id(image.getId())
                .originImageName(image.getOriginImageName())
                .imageName(image.getImageName())
                .imageUrl(image.getImageUrl())
                .isPending(image.isPending())
                .build();
    }

    public Image toModel() {
        return Image.builder()
                .id(this.id)
                .memory(this.memory == null ? null : this.memory.toModel())
                .originImageName(this.originImageName)
                .imageName(this.imageName)
                .imageUrl(this.imageUrl)
                .isPending(this.isPending)
                .build();
    }

    public Image pureToModel() {
        return Image.builder()
                .id(this.id)
                .originImageName(this.originImageName)
                .imageName(this.imageName)
                .imageUrl(this.imageUrl)
                .isPending(this.isPending)
                .build();
    }

    public Optional<MemoryEntity> getMemory() {
        return Optional.ofNullable(this.memory);
    }
}
