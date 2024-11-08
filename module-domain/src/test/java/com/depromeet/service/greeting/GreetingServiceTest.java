package com.depromeet.service.greeting;

import static org.assertj.core.api.Assertions.*;

import com.depromeet.greeting.domain.Greeting;
import com.depromeet.greeting.port.out.AIPort;
import com.depromeet.greeting.port.out.GreetingCachePort;
import com.depromeet.greeting.service.GreetingService;
import com.depromeet.mock.greeting.FakeAIManager;
import com.depromeet.mock.greeting.FakeGreetingCacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GreetingServiceTest {
    private AIPort aiPort;
    private GreetingCachePort greetingCachePort;
    private GreetingService greetingService;

    @BeforeEach
    void init() {
        aiPort = new FakeAIManager();
        greetingCachePort = new FakeGreetingCacheManager();
        greetingService = new GreetingService(aiPort, greetingCachePort);
    }

    @Test
    public void 인삿말을_조회합니다() throws Exception {
        // when
        Greeting greeting = greetingService.getGreeting();

        // then
        assertThat(greeting.getMessage()).isEqualTo("Hello, World!");
    }
}
