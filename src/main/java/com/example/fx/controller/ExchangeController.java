package com.example.fx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeController {

    @GetMapping("/rate")
    public String getExchangeRate(@RequestParam String from, @RequestParam String to) {

        return "Requested rate from " + from + " to " + to;
    }
}
