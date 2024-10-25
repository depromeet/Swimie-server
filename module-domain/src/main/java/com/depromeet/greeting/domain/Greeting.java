package com.depromeet.greeting.domain;

import lombok.Getter;

@Getter
public class Greeting {
    private String message;

    public Greeting(String message) {
        this.message = message;
    }
}
