package com.depromeet.memory.service;

import com.depromeet.exception.ForbiddenException;
import com.depromeet.type.memory.MemoryErrorType;

public class MemoryValidator {
    public static void validatePermission(Long memoryMemberId, Long requestMemberId) {
        if (!memoryMemberId.equals(requestMemberId)) {
            throw new ForbiddenException(MemoryErrorType.FORBIDDEN);
        }
    }

    public static Boolean isMyMemory(Long memoryMemberId, Long requestMemberId) {
        return memoryMemberId.equals(requestMemberId);
    }
}
