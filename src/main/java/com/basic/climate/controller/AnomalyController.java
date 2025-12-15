package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.service.AnomalyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anomaly")
@RequiredArgsConstructor
public class AnomalyController {

    private final AnomalyService anomalyService;


    @GetMapping("/states")
    public Result<?> getStateAnomalies() {
        return Result.ok(anomalyService.getStateAnomalyTree());
    }


    @GetMapping("/counties")
    public Result<?> getCountyAnomalies() {
        return Result.ok(anomalyService.getCountyAnomalies());
    }
}
