package com.depromeet.memory.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.ImageUploadStatus;
import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.depromeet.image.service.ImageDeleteService;
import com.depromeet.image.service.ImageGetService;
import com.depromeet.image.service.ImageUpdateService;
import com.depromeet.image.service.ImageUploadService;
import com.depromeet.member.Member;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.fixture.*;
import com.depromeet.memory.mock.FakeImageRepository;
import com.depromeet.memory.mock.FakeS3ImageManager;
import com.depromeet.pool.domain.Pool;
import com.depromeet.util.ImageNameUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageServiceTest {
    private FakeS3ImageManager s3ImageManager;
    private FakeImageRepository imageRepository;

    private ImageGetService imageGetService;
    private ImageUploadService imageUploadService;
    private ImageUpdateService imageUpdateService;
    private ImageDeleteService imageDeleteService;

    private Long memberId = 1L;
    private Long memoryDetailId = 1L;
    private Long memoryId = 1L;

    private final Pool pool = PoolFixture.make("testPool", "test address", 25);
    private Member member = MemberFixture.make(memberId, "USER");
    private MemoryDetail memoryDetail = MemoryDetailFixture.make(memoryDetailId);
    private Memory memory = MemoryFixture.make(memoryId, member, pool, memoryDetail);

    List<String> originImageNames = List.of("image1.png", "image2.png", "image3.png");

    @BeforeEach
    void init() {
        s3ImageManager = new FakeS3ImageManager();
        imageRepository = new FakeImageRepository();

        imageGetService = new ImageGetService(imageRepository);
        imageUploadService = new ImageUploadService(imageRepository, s3ImageManager);
        imageUpdateService = new ImageUpdateService(imageRepository, s3ImageManager);
        imageDeleteService = new ImageDeleteService(imageRepository, s3ImageManager);
    }

    @Test
    void ImagePresignedUrlVo을_리턴한다() {
        // given
        List<ImagePresignedUrlVo> expectedImagePresignedUrlVos = new ArrayList<>();

        for (String originImageName : originImageNames) {
            String imageName = ImageNameUtil.createImageName(originImageName);

            ImagePresignedUrlVo imagePresignedUrlVo =
                    ImagePresignedUrlVo.builder()
                            .imageName(originImageName)
                            .presignedUrl(s3ImageManager.getPresignedUrl(imageName))
                            .build();
            expectedImagePresignedUrlVos.add(imagePresignedUrlVo);
        }

        // when
        List<ImagePresignedUrlVo> actualImagePresignedUrlVos =
                imageUploadService.getPresignedUrlAndSaveImages(originImageNames);

        // then
        List<Long> actualImageIds =
                actualImagePresignedUrlVos.stream().map(ImagePresignedUrlVo::imageId).toList();

        assertThat(actualImageIds).isNotEmpty();

        IntStream.range(0, expectedImagePresignedUrlVos.size())
                .forEach(
                        i ->
                                assertPresignedUrlVoMatches(
                                        expectedImagePresignedUrlVos.get(i),
                                        actualImagePresignedUrlVos.get(i)));
    }

    private void assertPresignedUrlVoMatches(
            ImagePresignedUrlVo expected, ImagePresignedUrlVo actual) {
        assertThat(actual.imageName()).isEqualTo(expected.imageName());
        assertThat(actual.presignedUrl()).isEqualTo(expected.presignedUrl());
    }

    @Test
    void 이미지_업로드_상태_변경_및_이미지에_수영기록을_추가한다() {
        // given
        List<Image> images =
                ImageFixture.makeImages(originImageNames, null, ImageUploadStatus.PENDING);
        images = imageRepository.saveAll(images);

        List<Long> imageIds = images.stream().map(Image::getId).toList();

        // when
        imageUploadService.changeImageStatusAndAddMemoryIdToImages(memory, imageIds);

        // then
        List<Image> updatedImages = imageRepository.findImageByIds(imageIds);
        for (Image updatedImage : updatedImages) {
            assertThat(updatedImage.getImageUploadStatus()).isEqualTo(ImageUploadStatus.UPLOADED);
        }
    }

    @Test
    void 수영기록의_이미지를_수정한다() {
        // given
        List<Image> images =
                ImageFixture.makeImages(originImageNames, memory, ImageUploadStatus.UPLOADED);
        imageRepository.saveAll(images);
        List<String> updateOriginImageNames = List.of("image1.png", "image3.png", "image4.png");

        // when
        List<ImagePresignedUrlVo> actualImagePresignedUrlVos =
                imageUpdateService.updateImages(memory, updateOriginImageNames);

        // then
        List<String> actualImageNames =
                actualImagePresignedUrlVos.stream().map(ImagePresignedUrlVo::imageName).toList();

        assertThat(actualImageNames).containsExactlyInAnyOrderElementsOf(updateOriginImageNames);
    }

    @Test
    void 이미지_업로드_상태를_변경한다() {
        // given
        List<Image> images =
                ImageFixture.makeImages(originImageNames, memory, ImageUploadStatus.PENDING);
        images = imageRepository.saveAll(images);
        List<Long> imageIds = images.stream().map(Image::getId).toList();

        // when
        imageUpdateService.changeImageStatus(imageIds);

        // then
        List<Image> updatedImages = imageRepository.findImageByIds(imageIds);

        for (Image updatedImage : updatedImages) {
            assertThat(updatedImage.getImageUploadStatus()).isEqualTo(ImageUploadStatus.UPLOADED);
        }
    }

    @Test
    void memoryId에_해당하는_이미지를_조회한다() {
        // given
        List<Image> images =
                ImageFixture.makeImages(originImageNames, memory, ImageUploadStatus.UPLOADED);
        images = imageRepository.saveAll(images);

        // when
        List<Image> actualImages = imageGetService.findImagesByMemoryId(memory.getId());

        // then
        assertThat(actualImages).containsExactlyInAnyOrderElementsOf(images);
    }

    @Test
    void 수영기록의_이미지를_삭제한다() {
        // given
        Image image = ImageFixture.make("originImage.png", memory, ImageUploadStatus.UPLOADED);
        Long imageId = imageRepository.save(image);

        // when
        imageRepository.deleteById(imageId);

        // then
        assertThat(imageRepository.findById(imageId).isEmpty()).isTrue();
    }

    @Test
    void memoryId_로_이미지를_삭제한다() {
        // given
        List<Image> images =
                ImageFixture.makeImages(originImageNames, memory, ImageUploadStatus.UPLOADED);
        images = imageRepository.saveAll(images);
        List<Long> imageIds = images.stream().map(Image::getId).toList();

        // when
        imageDeleteService.deleteAllImagesByMemoryId(memory.getId());

        // then
        List<Image> deletedImages = imageRepository.findImageByIds(imageIds);
        assertThat(deletedImages).isEmpty();
    }
}
