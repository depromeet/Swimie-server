package com.depromeet.memory.domain.vo;

import com.depromeet.memory.domain.Memory;

public record MemoryInfo(Memory memory, Long prevId, Long nextId, Boolean isMyMemory) {}
