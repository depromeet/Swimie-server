package com.depromeet.fixture;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class ClockFixture extends Clock {

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this;
    }

    @Override
    public Instant instant() {
        return Instant.parse("2024-07-01T10:00:00Z");
    }
}
