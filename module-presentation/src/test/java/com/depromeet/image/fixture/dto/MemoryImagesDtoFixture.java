package com.depromeet.image.fixture.dto;

import com.depromeet.image.dto.response.ImageResponse;
import java.util.ArrayList;
import java.util.List;

public class MemoryImagesDtoFixture {
    public static List<ImageResponse> make() {
        List<ImageResponse> imagesRespons = new ArrayList<ImageResponse>();
        for (int i = 0; i < 3; i++) {
            ImageResponse imageResponse =
                    ImageResponse.builder()
                            .imageId((long) (i + 1))
                            .originImageName("originImage" + i + ".png")
                            .imageName("image" + i + ".png")
                            .url("http://image" + i + ".png")
                            .build();
            imagesRespons.add(imageResponse);
        }
        return imagesRespons;
    }
}
