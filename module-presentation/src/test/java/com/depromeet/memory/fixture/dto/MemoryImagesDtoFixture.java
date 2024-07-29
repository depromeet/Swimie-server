package com.depromeet.memory.fixture.dto;

import com.depromeet.image.dto.response.ImagesResponse;
import java.util.ArrayList;
import java.util.List;

public class MemoryImagesDtoFixture {
    public static List<ImagesResponse> make() {
        List<ImagesResponse> imagesRespons = new ArrayList<ImagesResponse>();
        for (int i = 0; i < 3; i++) {
            ImagesResponse imagesResponse =
                    ImagesResponse.builder()
                            .imageId((long) (i + 1))
                            .originImageName("originImage" + i + ".png")
                            .imageName("image" + i + ".png")
                            .url("http://image" + i + ".png")
                            .build();
            imagesRespons.add(imagesResponse);
        }
        return imagesRespons;
    }
}
