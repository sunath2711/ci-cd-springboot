package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${app.version}")
    private String version;

    @Value("${BUILD_NUMBER:local}")
    private String buildNumber;

    @Value("${ENVIRONMENT:dev}")
    private String environment;

    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> response = new HashMap<>();
        response.put("service", serviceName);
        response.put("version", version);
        response.put("buildNumber", buildNumber);
        response.put("environment", environment);
        return response;
    }
}
