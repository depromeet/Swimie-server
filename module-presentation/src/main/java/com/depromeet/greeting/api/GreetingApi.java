package com.depromeet.greeting.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Greeting")
public interface GreetingApi {
    @Operation(summary = "메인 화면 인삿말 조회")
    ResponseEntity<?> getGreeting();
}
