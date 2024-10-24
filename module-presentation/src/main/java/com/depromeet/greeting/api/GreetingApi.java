package com.depromeet.greeting.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.greeting.dto.response.GreetingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Greeting")
public interface GreetingApi {
    @Operation(summary = "메인 화면 인삿말 조회")
    ApiResponse<GreetingResponse> getGreeting();
}
