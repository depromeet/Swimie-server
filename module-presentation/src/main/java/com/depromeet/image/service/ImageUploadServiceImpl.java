package com.depromeet.image.service;

import com.depromeet.image.Image;
import com.depromeet.image.dto.request.ImagesMemoryIdDto;
import com.depromeet.image.repository.ImageRepository;
import com.depromeet.memory.Memory;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.util.ImageNameUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {
    private final ImageRepository imageRepository;
    private final MemoryRepository memoryRepository;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public List<Long> uploadMemoryImages(List<MultipartFile> files) {
        //        List<MultipartFile> memoryImages = imageUploadDto.images();
        List<MultipartFile> memoryImages = files; // 임시
        List<Image> images = uploadImagesAndGetImages(memoryImages);

        return imageRepository.saveAll(images);
    }

    @Override
    public void addMemoryIdToImages(ImagesMemoryIdDto inputImagesMemoryIdDto) {
        Long memoryId = inputImagesMemoryIdDto.memoryId();
        Memory memory =
                memoryRepository
                        .findById(memoryId)
                        .orElseThrow(() -> new NoSuchElementException("Memory not found"));
        List<Long> imageIds = inputImagesMemoryIdDto.imageIds();

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

                String originalImageName = multipartFile.getOriginalFilename();
                String contentType = multipartFile.getContentType();
                long imageSize = multipartFile.getSize();

                String imageName =
                        ImageNameUtil.createImageName(originalImageName, contentType, imageSize);
                uploadImage(multipartFile, contentType, imageSize, imageName);

                String imageUrl = getImageUrl(imageName);

                Image image = Image.builder().imageName(imageName).imageUrl(imageUrl).build();
                images.add(image);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return images;
    }

    private void uploadImage(
            MultipartFile multipartFile, String contentType, long imageSize, String imageName)
            throws IOException {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .contentType(contentType)
                        .contentLength(imageSize)
                        .key(imageName)
                        .build();

        RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());

        s3Client.putObject(putObjectRequest, requestBody);
    }

    private String getImageUrl(String imageName) {
        GetUrlRequest getUrlRequest =
                GetUrlRequest.builder().bucket(bucketName).key(imageName).build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }
}
