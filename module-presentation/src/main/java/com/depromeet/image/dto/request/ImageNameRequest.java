package com.depromeet.image.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ImageNameRequest(@NotNull List<String> imageNames) {}
