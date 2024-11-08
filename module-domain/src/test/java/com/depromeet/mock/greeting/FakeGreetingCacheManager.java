package com.depromeet.mock.greeting;

import com.depromeet.greeting.domain.Greeting;
import com.depromeet.greeting.port.out.GreetingCachePort;
import java.util.HashMap;
import java.util.Map;

public class FakeGreetingCacheManager implements GreetingCachePort {
    private static final String GREETING_KEY = "greeting";
    private Map<String, String> data = new HashMap<>();

    @Override
    public Greeting saveGreeting(Greeting greeting) {
        if (greeting.getMessage() != null) {
            data.put(GREETING_KEY, greeting.getMessage());
        }
        return greeting;
    }

    @Override
    public Greeting getGreeting() {
        return new Greeting(data.get(GREETING_KEY));
    }
}
