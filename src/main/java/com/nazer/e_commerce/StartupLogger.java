package com.nazer.e_commerce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {

    @Value("${server.port}")
    private String port;

    @EventListener(ApplicationReadyEvent.class)
    public void logPort() {
        System.out.println("✅ Server is running on port: " + port);
    }
}