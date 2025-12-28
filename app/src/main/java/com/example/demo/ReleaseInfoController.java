package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ReleaseInfoController {

    @Value("${APP_VERSION:local}")
    private String appVersion;

    @Value("${APP_NAME:release-info-service}")
    private String appName;

    @GetMapping("/release")
    public Map<String, String> releaseInfo() {
        return Map.of(
                "application", appName,
                "version", appVersion
        );
    }
}
