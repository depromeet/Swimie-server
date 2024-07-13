package com.depromeet.image.dto.request;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ImageUploadDto(List<MultipartFile> images) {}
