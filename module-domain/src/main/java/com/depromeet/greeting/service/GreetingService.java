package com.depromeet.greeting.service;

import com.depromeet.greeting.domain.Greeting;
import com.depromeet.greeting.port.in.usecase.GreetingUseCase;
import com.depromeet.greeting.port.out.AIPort;
import com.depromeet.greeting.port.out.GreetingCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingService implements GreetingUseCase {
    private final AIPort aiPort;
    private final GreetingCachePort greetingCachePort;

    @Override
    public Greeting getGreeting() {
        Greeting greeting = greetingCachePort.getGreeting();
        if (invalidGreeting(greeting)) {
            String message = aiPort.getChatCompletions();
            return greetingCachePort.saveGreeting(new Greeting(message));
        }
        return greeting;
    }

    private boolean invalidGreeting(Greeting greeting) {
        return greeting.getMessage() == null;
    }
}
