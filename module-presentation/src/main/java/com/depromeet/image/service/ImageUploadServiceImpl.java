package com.depromeet.image.service;

import static com.depromeet.type.common.CommonErrorType.INTERNAL_SERVER;

import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.image.Image;
import com.depromeet.image.repository.ImageRepository;
import com.depromeet.memory.Memory;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.type.image.ImageErrorType;
import com.depromeet.util.ImageNameUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {
    private final ImageRepository imageRepository;
    private final MemoryRepository memoryRepository;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud-front.domain}")
    private String domain;

    @Override
    public List<Long> uploadMemoryImages(List<MultipartFile> memoryImages) {
        if (memoryImages.isEmpty()) {
            throw new BadRequestException(ImageErrorType.IMAGES_CANNOT_BE_NULL);
        }
        List<Image> images = uploadImagesAndGetImages(memoryImages);

        return imageRepository.saveAll(images).stream().map(Image::getId).toList();
    }

    @Override
    public void addMemoryIdToImages(Memory memory, List<Long> imageIds) {
        List<Image> images = imageRepository.findImageByIds(imageIds);
        for (Image image : images) {
            image.addMemoryToImage(memory);
        }
        imageRepository.saveAll(images);
    }

    private List<Image> uploadImagesAndGetImages(List<MultipartFile> memoryImages) {
        List<Image> images = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : memoryImages) {
                String originImageName = multipartFile.getOriginalFilename();
                if (originImageName == null || originImageName.isEmpty()) {
                    throw new BadRequestException(ImageErrorType.INVALID_IMAGE_NAME);
                }

                String contentType = multipartFile.getContentType();
                long imageSize = multipartFile.getSize();

                String imageName = ImageNameUtil.createImageName(originImageName);
                uploadImage(multipartFile, contentType, imageSize, imageName);

                String imageUrl = generateImageUrl(imageName);

                Image image =
                        Image.builder()
                                .originImageName(originImageName)
                                .imageName(imageName)
                                .imageUrl(imageUrl)
                                .build();
                images.add(image);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerException(INTERNAL_SERVER);
        }
        return images;
    }

    private void uploadImage(
            MultipartFile multipartFile, String contentType, long imageSize, String imageName)
            throws IOException {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .contentLength(imageSize)
                        .contentType(contentType)
                        .key(imageName)
                        .build();

        byte[] imageByte = multipartFile.getBytes();
        InputStream inputStream = new ByteArrayInputStream(imageByte);
        RequestBody requestBody = RequestBody.fromInputStream(inputStream, imageByte.length);
        s3Client.putObject(putObjectRequest, requestBody);
    }

    private String generateImageUrl(String imageName) {
        return domain + "/" + imageName;
    }
}
