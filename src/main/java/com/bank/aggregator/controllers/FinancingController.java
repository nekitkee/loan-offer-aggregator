package com.bank.aggregator.controllers;

import com.bank.aggregator.models.common.Application;
import com.bank.aggregator.models.common.ApplicationRequest;
import com.bank.aggregator.services.FinancingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class FinancingController {
    private final FinancingService financingService;
    @PostMapping("/apply")
    public Application apply(@Valid @RequestBody ApplicationRequest request) {
        try {
            return financingService.apply(request);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while processing the application", e);
        }
    }
}
