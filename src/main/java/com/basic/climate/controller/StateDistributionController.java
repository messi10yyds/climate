package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.service.StateDistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StateDistributionController {

    private final StateDistributionService stateDistributionService;

    @GetMapping("/api/state/distribution")
    public Result<?> getDistribution(@RequestParam Integer year) {
        return Result.ok(stateDistributionService.getStateDistribution(year));
    }
}
