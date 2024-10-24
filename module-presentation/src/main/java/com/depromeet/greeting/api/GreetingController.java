package com.depromeet.greeting.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.greeting.dto.response.GreetingResponse;
import com.depromeet.greeting.facade.GreetingFacade;
import com.depromeet.type.ai.AISuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/greeting")
public class GreetingController implements GreetingApi {
    private final GreetingFacade greetingFacade;

    @GetMapping
    public ApiResponse<GreetingResponse> getGreeting() {
        GreetingResponse response = greetingFacade.getGreeting();
        return ApiResponse.success(AISuccessType.GET_RESPONSE_SUCCESS, response);
    }
}
