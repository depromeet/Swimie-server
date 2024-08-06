package com.depromeet.redis.repository;

import com.depromeet.auth.port.out.persistence.RefreshRedisPersistencePort;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class RefreshRedisRepository implements RefreshRedisPersistencePort {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setData(Long memberId, String refreshToken, Long expireTime) {
        redisTemplate
                .opsForValue()
                .set(memberId.toString(), refreshToken, expireTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getData(Long memberId) {
        return redisTemplate.opsForValue().get(memberId.toString());
    }

    @Override
    public void deleteData(Long memberId) {
        redisTemplate.delete(memberId.toString());
    }
}
