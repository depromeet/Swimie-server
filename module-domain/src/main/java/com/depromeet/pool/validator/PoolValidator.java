package com.depromeet.pool.validator;

import com.depromeet.exception.ForbiddenException;
import com.depromeet.type.pool.PoolErrorType;

public class PoolValidator {
    public static boolean validateOwnerOfFavorite(Long memberId, Long favoritePoolMemberId) {
        if (!memberId.equals(favoritePoolMemberId)) {
            throw new ForbiddenException(PoolErrorType.FAVORITE_FORBIDDEN);
        }
        return true;
    }
}
