package com.depromeet.memory.dto.response;

public record MemoryCreateResponse(int rank, Long memoryId) {
    public static MemoryCreateResponse of(int rank, Long memoryId) {
        return new MemoryCreateResponse(rank, memoryId);
    }
}
