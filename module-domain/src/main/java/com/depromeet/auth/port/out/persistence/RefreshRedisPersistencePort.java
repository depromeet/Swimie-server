package com.depromeet.auth.port.out.persistence;

public interface RefreshRedisPersistencePort {
    void setData(Long memberId, String refreshToken, Long expireTime);

    String getData(Long memberId);

    void deleteData(Long memberId);
}
