package com.depromeet.job.image.processor;

import com.depromeet.image.entity.ImageEntity;
import com.depromeet.manage.BatchS3ImageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@StepScope
@Component
@RequiredArgsConstructor
public class ImageItemProcessor implements ItemProcessor<ImageEntity, ImageEntity> {
    private final BatchS3ImageManager imageManager;

    @Override
    public ImageEntity process(ImageEntity item) throws Exception {
        String imageName = item.getImageName();
        imageManager.deleteImageFromS3(imageName);
        return item;
    }
}
