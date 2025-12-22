package com.example.auth.controller;

import com.example.auth.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final IntegrationService integrationService;

    @GetMapping("/test")
    public String login(){
        return integrationService.hitApiv2();
    }
}
