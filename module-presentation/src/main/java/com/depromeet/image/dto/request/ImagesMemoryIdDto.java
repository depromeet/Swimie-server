package com.depromeet.image.dto.request;

import java.util.List;

public record ImagesMemoryIdDto(Long memoryId, List<Long> imageIds) {}
