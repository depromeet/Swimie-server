package com.depromeet.memory.dto.response;

import lombok.Builder;

public record StrokeResponseDto(Long strokeId, String name, Short laps, Integer meter) {
    @Builder
    public StrokeResponseDto {}
}
