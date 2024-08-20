package com.depromeet.image.repository;

import static com.depromeet.image.entity.QImageEntity.imageEntity;
import static com.depromeet.memory.entity.QMemoryEntity.memoryEntity;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.image.entity.ImageEntity;
import com.depromeet.image.entity.QImageEntity;
import com.depromeet.image.port.out.persistence.ImagePersistencePort;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageRepository implements ImagePersistencePort {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final ImageJpaRepository imageJpaRepository;

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
                .update(imageEntity)
                .set(imageEntity.imageUploadStatus, ImageUploadStatus.UPLOADED)
                .where(imageEntity.id.in(imageIds))
                .execute();
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageJpaRepository.findById(id).map(ImageEntity::toModel);
    }

    @Override
    public List<Image> findAllByMemoryId(Long memoryId) {
        List<ImageEntity> imageEntities =
                queryFactory
                        .selectFrom(imageEntity)
                        .join(imageEntity.memory, memoryEntity)
                        .fetchJoin()
                        .where(imageEntity.memory.id.eq(memoryId))
                        .fetch();
        return imageEntities.stream().map(ImageEntity::toModel).toList();
    }

    @Override
    public List<Image> findAllByMemoryIdAndHasUploaded(Long memoryId) {
        List<ImageEntity> images =
                queryFactory
                        .selectFrom(imageEntity)
                        .where(
                                imageEntity.memory.id.eq(memoryId),
                                imageEntity.imageUploadStatus.eq(ImageUploadStatus.UPLOADED))
                        .fetch();
        return images.stream().map(ImageEntity::toModel).toList();
    }

    @Override
    public List<Image> findImageByIds(List<Long> ids) {
        List<ImageEntity> imageEntities =
                queryFactory.selectFrom(imageEntity).where(imageEntity.id.in(ids)).fetch();
        return imageEntities.stream().map(ImageEntity::toModel).toList();
    }

    @Override
    public void deleteById(Long id) {
        imageJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        imageJpaRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public void deleteAllByMemoryId(Long memoryId) {
        imageJpaRepository.deleteAllByMemoryId(memoryId);
    }

    @Override
    public void setNullByMemoryIds(List<Long> memoryIds) {
        queryFactory.update(imageEntity)
                .setNull(imageEntity.memory.id)
                .where(imageEntity.memory.id.in(memoryIds))
                .execute();
        em.flush();
        em.clear();
    }
}
