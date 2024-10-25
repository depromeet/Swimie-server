package com.depromeet.greeting.port.out;

import com.depromeet.greeting.domain.Greeting;

public interface GreetingCachePort {
    Greeting saveGreeting(Greeting greeting);

    Greeting getGreeting();
}
