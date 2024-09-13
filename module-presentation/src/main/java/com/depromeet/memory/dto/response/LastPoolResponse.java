package com.depromeet.memory.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LastPoolResponse(
        @Schema(
                        description = "수영장 ID",
                        example = "1",
                        type = "long",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Long id,
        @Schema(
                        description = "수영장 이름",
                        example = "스위미 수영장",
                        type = "string",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String name) {}
