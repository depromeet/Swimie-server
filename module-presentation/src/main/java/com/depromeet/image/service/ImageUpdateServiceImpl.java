package com.depromeet.image.service;

import static com.depromeet.type.common.CommonErrorType.INTERNAL_SERVER;

import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.image.Image;
import com.depromeet.image.repository.ImageRepository;
import com.depromeet.memory.Memory;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.type.image.ImageErrorType;
import com.depromeet.util.ImageNameUtil;
import java.io.IOException;
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
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageUpdateServiceImpl implements ImageUpdateService {
    private final ImageRepository imageRepository;
    private final MemoryRepository memoryRepository;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public void updateImages(Long memoryId, List<MultipartFile> images) {
        try {
            List<Image> existingImages = imageRepository.findImagesByMemoryId(memoryId);
            List<String> existingImageNames = getExistingImageNames(existingImages);

            List<String> updatedImageNames = updateNewImages(memoryId, images, existingImageNames);
            deleteNonUpdatedImages(existingImages, updatedImageNames);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerException(INTERNAL_SERVER);
        }
    }

    private List<String> getExistingImageNames(List<Image> existingImages) {
        return existingImages.stream().map(Image::getImageName).toList();
    }

    private List<String> updateNewImages(
            Long memoryId, List<MultipartFile> images, List<String> existImagesName)
            throws IOException {

        Memory memory = getMemory(memoryId);
        List<String> updatedImageNames = new ArrayList<>();

        for (MultipartFile image : images) {
            String imageName = generateImageName(image);
            updatedImageNames.add(imageName);

            if (existImagesName.contains(imageName)) continue;

            String imageUrl = upload(imageName, image);
            saveNewImage(imageName, imageUrl, memory);
        }
        return updatedImageNames;
    }

    private Memory getMemory(Long memoryId) {
        return memoryRepository
                .findById(memoryId)
                .orElseThrow(() -> new NotFoundException(INTERNAL_SERVER)); // 임시
    }

    private String generateImageName(MultipartFile image) {
        String originImageName = image.getOriginalFilename();
        if (originImageName == null || originImageName.isEmpty()) {
            throw new BadRequestException(ImageErrorType.INVALID_IMAGE_NAME);
        }

        return ImageNameUtil.createImageName(originImageName);
    }

    private void saveNewImage(String imageName, String imageUrl, Memory memory) {
        Image newImage =
                Image.builder().imageName(imageName).imageUrl(imageUrl).memory(memory).build();
        imageRepository.save(newImage);
    }

    private String upload(String imageName, MultipartFile image) throws IOException {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .contentType(image.getContentType())
                        .contentLength(image.getSize())
                        .key(imageName)
                        .build();

        RequestBody requestBody = RequestBody.fromBytes(image.getBytes());

        s3Client.putObject(putObjectRequest, requestBody);

        GetUrlRequest getUrlRequest =
                GetUrlRequest.builder().bucket(bucketName).key(imageName).build();
        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }

    private void deleteNonUpdatedImages(List<Image> existImages, List<String> updatedImageNames) {
        List<Long> deletedImageIds =
                existImages.stream()
                        .filter(i -> !updatedImageNames.contains(i.getImageName()))
                        .map(Image::getId)
                        .toList();
        if (!deletedImageIds.isEmpty()) {
            imageRepository.deleteAllByIds(deletedImageIds);
        }
    }
}
