package com.depromeet.redis.repository;

import com.depromeet.auth.port.out.persistence.SocialRedisPersistencePort;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SocialRedisRepository implements SocialRedisPersistencePort {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setATData(String providerId, String accessToken, Long expireTime) {
        redisTemplate
                .opsForValue()
                .set("AT(oauth2):" + providerId, accessToken, expireTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getATData(String providerId) {
        return redisTemplate.opsForValue().get("AT(oauth2):" + providerId);
    }

    @Override
    public void deleteATData(String providerId) {
        redisTemplate.delete("AT(oauth2):" + providerId);
    }

    @Override
    public void setRTData(String providerId, String refreshToken, Long expireTime) {
        redisTemplate
                .opsForValue()
                .set("RT(oauth2):" + providerId, refreshToken, expireTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getRTData(String providerId) {
        return redisTemplate.opsForValue().get("RT(oauth2):" + providerId);
    }

    @Override
    public void deleteRTData(String providerId) {
        redisTemplate.delete("RT(oauth2):" + providerId);
    }
}
