package com.onixx.apolloveiculos.api.Infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentChecker {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    public boolean isDevelopment() {
        return "dev".equalsIgnoreCase(activeProfile);
    }

    public boolean isProduction() {
        return "prod".equalsIgnoreCase(activeProfile);
    }

    public String getActiveProfile() {
        return activeProfile;
    }
}