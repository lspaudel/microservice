package com.example.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/core")
    public String coreFallback() {
        return "Core service is currently unavailable. Please try again later.";
    }

    @GetMapping("/fallback/auth")
    public String authFallback() {
        return "Auth service is currently unavailable. Please try again later.";
    }
}
