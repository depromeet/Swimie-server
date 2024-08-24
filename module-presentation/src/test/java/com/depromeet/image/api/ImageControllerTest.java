package com.depromeet.image.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.image.dto.request.ImageNameRequest;
import com.depromeet.image.dto.response.ImageResponse;
import com.depromeet.image.dto.response.ImageUploadResponse;
import com.depromeet.image.facade.ImageFacade;
import com.depromeet.image.fixture.dto.ImageUploadResponseDtoFixture;
import com.depromeet.image.fixture.dto.MemoryImagesDtoFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = ImageController.class)
public class ImageControllerTest extends ControllerTestConfig {
    @Autowired private ObjectMapper objectMapper;

    @MockBean ImageFacade imageFacade;

    @Test
    @WithCustomMockMember
    void 이미지_PresignedUrl을_발급한다() throws Exception {
        // given
        Map<String, Object> requestBody = new HashMap<>();

        List<String> imageNames = List.of("image1.png", "image2.png", "image3.png");
        requestBody.put("imageNames", imageNames);

        List<ImageUploadResponse> images = ImageUploadResponseDtoFixture.make(imageNames);

        // when
        when(imageFacade.getPresignedUrlAndSaveImages(any(ImageNameRequest.class)))
                .thenReturn(images);

        // then
        mockMvc.perform(
                        post("/image/presigned-url")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("IMAGE_1"))
                .andExpect(jsonPath("$.message").value("이미지 업로드에 사용할 presignedURL 생성에 성공하였습니다"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].imageId").value(images.get(0).imageId()))
                .andExpect(jsonPath("$.data[0].imageName").value(images.get(0).imageName()))
                .andExpect(jsonPath("$.data[0].presignedUrl").value(images.get(0).presignedUrl()))
                .andReturn();
    }

    @Test
    @WithCustomMockMember
    void 이미지를_수정한다() throws Exception {
        // given
        Map<String, Object> requestBody = new HashMap<>();

        List<String> imageNames = List.of("image1.png", "image2.png", "image3.png");
        requestBody.put("imageNames", imageNames);

        List<ImageUploadResponse> images = ImageUploadResponseDtoFixture.make(imageNames);

        // when
        when(imageFacade.updateImages(anyLong(), any(ImageNameRequest.class))).thenReturn(images);

        // then
        mockMvc.perform(
                        patch("/image/memory/{memoryId}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("IMAGE_4"))
                .andExpect(jsonPath("$.message").value("이미지 수정 및 추가된 이미지 presignedUrl 생성에 성공하였습니다"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].imageId").value(images.get(0).imageId()))
                .andExpect(jsonPath("$.data[0].imageName").value(images.get(0).imageName()))
                .andExpect(jsonPath("$.data[0].presignedUrl").value(images.get(0).presignedUrl()))
                .andReturn();
    }

    @Test
    @WithCustomMockMember
    void 이미지_상태변경을_한다() throws Exception {
        // given
        Map<String, Object> requestBody = new HashMap<>();
        List<Long> imageIds = List.of(1L, 2L, 3L);
        requestBody.put("imageIds", imageIds);

        // when
        doNothing().when(imageFacade).changeImageStatus(anyList());

        // then
        mockMvc.perform(
                        patch("/image/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("IMAGE_2"))
                .andExpect(jsonPath("$.message").value("이미지 업로드 status 변경에 성공하였습니다"))
                .andReturn();
    }

    @Test
    @WithCustomMockMember
    void memoryId로_이미지를_조회한다() throws Exception {
        // given
        Long memoryId = 1L;

        List<ImageResponse> images = MemoryImagesDtoFixture.make();

        // when
        when(imageFacade.findImagesByMemoryId(anyLong())).thenReturn(images);

        // then
        mockMvc.perform(get("/image/memory/{memoryId}", memoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("IMAGE_5"))
                .andExpect(jsonPath("$.message").value("이미지 조회에 성공하였습니다"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].imageId").value(images.get(0).imageId()))
                .andExpect(
                        jsonPath("$.data[0].originImageName")
                                .value(images.get(0).originImageName()))
                .andExpect(jsonPath("$.data[0].imageName").value(images.get(0).imageName()))
                .andExpect(jsonPath("$.data[0].url").value(images.get(0).url()))
                .andReturn();
    }

    @Test
    @WithCustomMockMember
    void memoryId로_이미지를_삭제한다() throws Exception {
        mockMvc.perform(delete("/image/memory/{memoryId}", 1))
                .andExpect(status().isOk())
                .andReturn();
    }
}
