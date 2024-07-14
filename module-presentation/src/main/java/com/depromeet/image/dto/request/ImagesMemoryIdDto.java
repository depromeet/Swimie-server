package com.depromeet.image.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "Image에 memory 할당")
public record ImagesMemoryIdDto(@Schema(name = "Image PK List") List<Long> imageIds) {}
