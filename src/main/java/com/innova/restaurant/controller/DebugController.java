package com.innova.restaurant.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {
    
    @Value("${spring.data.mongodb.uri:NOT_SET}")
    private String springMongoUri;
    
    @Value("${MONGODB_URI:NOT_SET_ENV}")
    private String envMongoUri;
    
    @Value("${spring.profiles.active:NOT_SET}")
    private String activeProfile;
    
    @GetMapping("/env")
    public Map<String, Object> getEnvironmentInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // Variables de Spring
        info.put("springMongoUri", springMongoUri);
        info.put("envMongoUri", envMongoUri);
        info.put("activeProfile", activeProfile);
        
        // Variables del sistema
        info.put("systemEnvMongoUri", System.getenv("MONGODB_URI"));
        info.put("systemPropertyMongoUri", System.getProperty("MONGODB_URI"));
        
        // Todas las variables del sistema que contengan MONGO
        Map<String, String> mongoVars = new HashMap<>();
        System.getenv().forEach((key, value) -> {
            if (key.toUpperCase().contains("MONGO")) {
                mongoVars.put(key, value);
            }
        });
        info.put("allMongoEnvVars", mongoVars);
        
        return info;
    }
}