package com.depromeet.memory.fixture.dto;

import com.depromeet.image.dto.response.MemoryImagesResponse;
import java.util.ArrayList;
import java.util.List;

public class MemoryImagesDtoFixture {
    public static List<MemoryImagesResponse> make() {
        List<MemoryImagesResponse> memoryImagesResponses = new ArrayList<MemoryImagesResponse>();
        for (int i = 0; i < 3; i++) {
            MemoryImagesResponse memoryImagesResponse =
                    MemoryImagesResponse.builder()
                            .imageId((long) (i + 1))
                            .originImageName("originImage" + i + ".png")
                            .imageName("image" + i + ".png")
                            .url("http://image" + i + ".png")
                            .build();
            memoryImagesResponses.add(memoryImagesResponse);
        }
        return memoryImagesResponses;
    }
}
