package com.depromeet.service;

import com.depromeet.greeting.domain.Greeting;
import com.depromeet.greeting.port.out.GreetingCachePort;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingCacheManager implements GreetingCachePort {
    private static final String GREETING_KEY = "greeting";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Greeting saveGreeting(Greeting greeting) {
        redisTemplate.opsForValue().set(GREETING_KEY, greeting.getMessage(), Duration.ofDays(1));
        return greeting;
    }

    @Override
    public Greeting getGreeting() {
        String message = redisTemplate.opsForValue().get(GREETING_KEY);
        return new Greeting(message);
    }
}
