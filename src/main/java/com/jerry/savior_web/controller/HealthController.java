package com.jerry.savior_web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 22454
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping("/heartBeat")
    public String heartBeat() {
        return "jump";
    }
}
