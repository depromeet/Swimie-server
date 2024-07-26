package com.depromeet.image.repository;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.image.entity.ImageEntity;
import com.depromeet.image.entity.QImageEntity;
import com.depromeet.image.port.out.persistence.ImagePersistencePort;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageRepository implements ImagePersistencePort {
    private final ImageJpaRepository imageJpaRepository;
    private final JPAQueryFactory queryFactory;

    QImageEntity image = QImageEntity.imageEntity;

    @Override
    public Long save(Image image) {
        ImageEntity imageEntity = imageJpaRepository.save(ImageEntity.from(image));
        return imageEntity.getId();
    }

    @Override
    public List<Image> saveAll(List<Image> images) {
        List<ImageEntity> memoryImageEntities = images.stream().map(ImageEntity::from).toList();

        return imageJpaRepository.saveAll(memoryImageEntities).stream()
                .map(ImageEntity::toModel)
                .toList();
    }

    @Override
    public void updateByImageIds(List<Long> imageIds) {
        queryFactory
                .update(image)
                .set(image.imageUploadStatus, ImageUploadStatus.UPLOADED)
                .where(image.id.in(imageIds));
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageJpaRepository.findById(id).map(ImageEntity::toModel);
    }

    @Override
    public List<Image> findAllByMemoryId(Long memoryId) {
        return imageJpaRepository.findByMemoryId(memoryId).stream()
                .map(ImageEntity::toModel)
                .toList();
    }

    @Override
    public List<Image> findAllByMemoryIdAndHasUploaded(Long memoryId) {
        List<ImageEntity> images =
                queryFactory
                        .selectFrom(image)
                        .where(
                                image.memory.id.eq(memoryId),
                                image.imageUploadStatus.eq(ImageUploadStatus.UPLOADED))
                        .fetch();

        return images.stream().map(ImageEntity::toModel).toList();
    }

    @Override
    public List<Image> findImageByIds(List<Long> ids) {
        return imageJpaRepository.findAllByIds(ids).stream().map(ImageEntity::toModel).toList();
    }

    @Override
    public void deleteById(Long id) {
        imageJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        imageJpaRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAllByMemoryId(Long memoryId) {
        imageJpaRepository.deleteAllByMemoryId(memoryId);
    }
}
