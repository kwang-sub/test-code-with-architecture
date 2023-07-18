package com.example.demo.mock;

import com.example.demo.common.sevice.port.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FakeClockHolder implements ClockHolder {
    private final Long time;

    @Override
    public long millis() {
        return time;
    }
}
