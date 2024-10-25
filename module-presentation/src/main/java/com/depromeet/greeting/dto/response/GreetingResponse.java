package com.depromeet.greeting.dto.response;

import com.depromeet.greeting.domain.Greeting;

public record GreetingResponse(String message) {
    public static GreetingResponse from(Greeting greeting) {
        return new GreetingResponse(greeting.getMessage());
    }
}
