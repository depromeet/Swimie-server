package com.depromeet.image.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ImageNameDto(@NotNull List<String> imageNames) {}
