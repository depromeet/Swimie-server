package com.depromeet.auth.port.out.persistence;

public interface SocialRedisPersistencePort {
    void setATData(String providerId, String accessToken, Long expireTime);

    String getATData(String providerId);

    void deleteATData(String providerId);

    void setRTData(String providerId, String refreshToken, Long expireTime);

    String getRTData(String providerId);

    void deleteRTData(String providerId);
}
