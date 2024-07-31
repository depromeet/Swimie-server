package com.depromeet.memory.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StrokeResponse(Long strokeId, String name, Float laps, Integer meter) {
    @Builder
    public StrokeResponse {}
}
