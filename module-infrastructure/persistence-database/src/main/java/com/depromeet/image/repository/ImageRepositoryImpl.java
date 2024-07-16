package com.depromeet.image.repository;

import com.depromeet.image.Image;
import com.depromeet.image.entity.ImageEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {
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
    public Optional<Image> findById(Long id) {
        return imageJpaRepository.findById(id).map(ImageEntity::toModel);
    }

    @Override
    public List<Image> findImagesByMemoryId(Long memoryId) {
        return imageJpaRepository.findByMemoryId(memoryId).stream()
                .map(ImageEntity::toModel)
                .toList();
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
