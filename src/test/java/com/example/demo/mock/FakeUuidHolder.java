package com.example.demo.mock;

import com.example.demo.common.sevice.port.UuidHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FakeUuidHolder implements UuidHolder {

    private final String uuid;
    @Override
    public String random() {
        return uuid;
    }
}
