package com.depromeet.greeting.facade;

import com.depromeet.greeting.domain.Greeting;
import com.depromeet.greeting.dto.response.GreetingResponse;
import com.depromeet.greeting.port.in.usecase.GreetingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GreetingFacade {
    private final GreetingUseCase greetingUseCase;

    public GreetingResponse getGreeting() {
        Greeting greeting = greetingUseCase.getGreeting();
        return GreetingResponse.from(greeting);
    }
}
