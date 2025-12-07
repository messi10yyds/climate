package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TrendController {

    private final TrendService trendService;

    @GetMapping("/api/trend")
    public Result<?> trend(
            @RequestParam String region,
            @RequestParam String type
    ) {
        List<Map<String, Object>> data;

        switch (type) {
            case "national" -> data = trendService.getNationalTrend();
            case "state" -> data = trendService.getStateTrend(region);
            case "county" -> data = trendService.getCountyTrend(region);
            default -> {
                return Result.fail("Invalid type. Must be national/state/county.");
            }
        }

        return Result.ok(data);
    }
}
