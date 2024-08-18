package com.depromeet.image.facade;

import com.depromeet.exception.BadRequestException;
import com.depromeet.image.domain.Image;
import com.depromeet.image.domain.vo.ImagePresignedUrlNameVo;
import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.depromeet.image.dto.request.ImageNameRequest;
import com.depromeet.image.dto.request.ProfileImageNameRequest;
import com.depromeet.image.dto.response.ImageResponse;
import com.depromeet.image.dto.response.ImageUploadResponse;
import com.depromeet.image.dto.response.ProfileImageUploadResponse;
import com.depromeet.image.port.in.ImageDeleteUseCase;
import com.depromeet.image.port.in.ImageGetUseCase;
import com.depromeet.image.port.in.ImageUpdateUseCase;
import com.depromeet.image.port.in.ImageUploadUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.service.MemberService;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.type.image.ImageErrorType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageFacade {
    private final MemoryService memoryService;
    private final MemberService memberService;
    private final ImageGetUseCase imageGetUseCase;
    private final ImageUploadUseCase imageUploadUseCase;
    private final ImageUpdateUseCase imageUpdateUseCase;
    private final ImageDeleteUseCase imageDeleteUseCase;

    public List<ImageUploadResponse> getPresignedUrlAndSaveImages(
            ImageNameRequest imageNameRequest) {
        if (imageNameRequest.imageNames() == null) {
            throw new BadRequestException(ImageErrorType.IMAGES_CANNOT_BE_EMPTY);
        }
        List<ImagePresignedUrlVo> imagePresignedUrlVos =
                imageUploadUseCase.getPresignedUrlAndSaveImages(imageNameRequest.imageNames());
        return imagePresignedUrlVos.stream().map(ImageUploadResponse::of).toList();
    }

    public ProfileImageUploadResponse getProflieImagePresignedUrlOrDeleteProfileImage(
            Long memberId, ProfileImageNameRequest profileImageNameRequest) {
        String imageName = profileImageNameRequest.imageName();
        if (imageName != null && !imageName.isBlank()) {
            // 새로운 이미지를 업로드할 수 있는 PresignedUrl 발급
            ImagePresignedUrlNameVo imagePresignedUrlVo =
                    imageUploadUseCase.getProfileImagePresignedUrl(
                            profileImageNameRequest.imageName());
            return ProfileImageUploadResponse.of(imagePresignedUrlVo);
        }
        // 전달 받은 이미지가 없는 경우 기존 이미지 삭제
        Member member = memberService.findById(memberId);
        if (member.getProfileImageUrl() != null && !member.getProfileImageUrl().isBlank()) {
            imageDeleteUseCase.deleteProfileImage(member.getProfileImageUrl());
        }
        // 멤버의 프로필 이미지 URL 정보를 수정한다
        memberService.updateProfileImageUrl(memberId, null);
        return null;
    }

    public List<ImageUploadResponse> updateImages(
            Long memoryId, ImageNameRequest imageNameRequest) {
        List<String> imageNames = new ArrayList<>();
        if (imageNameRequest.imageNames() != null) {
            imageNames = imageNameRequest.imageNames();
        }
        Memory memory = memoryService.findById(memoryId);
        List<ImagePresignedUrlVo> imagePresignedUrlVos =
                imageUpdateUseCase.updateImages(memory, imageNames);
        return imagePresignedUrlVos.stream().map(ImageUploadResponse::of).toList();
    }

    public void changeImageStatus(List<Long> imageIds) {
        imageUpdateUseCase.changeImageStatus(imageIds);
    }

    public void changeProfileImageUrl(Long memberId, String imageName) {
        // 존재하는 이미지를 지운다
        Member member = memberService.findById(memberId);
        if (member.getProfileImageUrl() != null && !member.getProfileImageUrl().isEmpty()) {
            imageDeleteUseCase.deleteProfileImage(member.getProfileImageUrl());
        }
        // 멤버의 프로필 이미지 URL 정보를 수정한다
        memberService.updateProfileImageUrl(memberId, imageName);
    }

    public List<ImageResponse> findImagesByMemoryId(Long memoryId) {
        List<Image> images = imageGetUseCase.findImagesByMemoryId(memoryId);
        return images.stream()
                .map(
                        image ->
                                ImageResponse.builder()
                                        .imageId(image.getId())
                                        .originImageName(image.getOriginImageName())
                                        .imageName(image.getImageName())
                                        .url(image.getImageUrl())
                                        .build())
                .toList();
    }

    public void deleteAllImagesByMemoryId(Long memoryId) {
        imageDeleteUseCase.deleteAllImagesByMemoryId(memoryId);
    }
}
