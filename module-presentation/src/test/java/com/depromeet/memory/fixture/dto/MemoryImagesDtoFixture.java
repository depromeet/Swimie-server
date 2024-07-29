package com.depromeet.memory.fixture.dto;

import com.depromeet.image.dto.response.MemoryImagesDto;
import java.util.ArrayList;
import java.util.List;

public class MemoryImagesDtoFixture {
    public static List<MemoryImagesDto> make() {
        List<MemoryImagesDto> memoryImagesDtos = new ArrayList<MemoryImagesDto>();
        for (int i = 0; i < 3; i++) {
            MemoryImagesDto memoryImagesDto =
                    MemoryImagesDto.builder()
                            .imageId((long) (i + 1))
                            .originImageName("originImage" + i + ".png")
                            .imageName("image" + i + ".png")
                            .url("http://image" + i + ".png")
                            .build();
            memoryImagesDtos.add(memoryImagesDto);
        }
        return memoryImagesDtos;
    }
}
