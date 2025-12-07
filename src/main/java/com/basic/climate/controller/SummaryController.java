package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.service.SummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/api/summary")
    public Result<Map<String, Object>> getSummary() {
        return Result.ok(summaryService.getSummary());
    }
}