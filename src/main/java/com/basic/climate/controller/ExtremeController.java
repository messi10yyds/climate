package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.service.ExtremeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExtremeController {

    private final ExtremeService extremeService;

    @GetMapping("/api/extreme")
    public Result<?> getExtreme(
            @RequestParam String region,
            @RequestParam String type
    ) {
        return switch (type) {
            case "national" -> Result.ok(extremeService.getNationalExtreme());
            case "state" -> Result.ok(extremeService.getStateExtreme(region));
            case "county" -> Result.ok(extremeService.getCountyExtreme(region));
            default -> Result.fail("Invalid type. Must be national/state/county.");
        };
    }

    @GetMapping("/api/extreme/national/year")
    public Result<?> getStateExtremeByYear(@RequestParam Integer year) {
        return Result.ok(extremeService.getStateExtremeByYear(year));
    }

    @GetMapping("/api/extreme/state/year")
    public Result<?> getCountyExtremeByYear(
            @RequestParam String state,
            @RequestParam Integer year
    ) {
        return Result.ok(extremeService.getCountyExtremeByYear(state, year));
    }

}
