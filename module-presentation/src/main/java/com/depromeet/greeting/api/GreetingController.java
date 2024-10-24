package com.depromeet.greeting.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.greeting.dto.response.GreetingResponse;
import com.depromeet.greeting.facade.GreetingFacade;
import com.depromeet.type.greeting.GreetingSuccessType;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/greeting")
public class GreetingController implements GreetingApi {
    private final GreetingFacade greetingFacade;

    @GetMapping
    public ResponseEntity<?> getGreeting() {
        GreetingResponse response = greetingFacade.getGreeting();
        CacheControl cacheControl = CacheControl.maxAge(Duration.ofHours(1)).cachePrivate();
        ApiResponse<GreetingResponse> apiResponse =
                ApiResponse.success(GreetingSuccessType.GET_RESPONSE_SUCCESS, response);
        return ResponseEntity.ok().cacheControl(cacheControl).body(apiResponse);
    }
}
