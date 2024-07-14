package com.depromeet.image.dto.request;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ImageUpdateDto(List<MultipartFile> images) {}
