package com.example.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoreController {
    @GetMapping("/info")
    public String info() {
        return "Core service info endpoint";
    }
}
