package com.depromeet.memory.dto.response;

public record MemoryCreateResponse(int month, int rank, Long memoryId) {
    public static MemoryCreateResponse of(int month, int rank, Long memoryId) {
        return new MemoryCreateResponse(month, rank, memoryId);
    }
}
