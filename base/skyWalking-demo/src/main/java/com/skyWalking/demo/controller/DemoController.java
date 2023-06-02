package com.skyWalking.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/echo")
    public void echo() {
        System.out.println("echo");
    }
}
