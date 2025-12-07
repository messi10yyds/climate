package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.service.StateCountyRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class StateCountyRankController {

    private final StateCountyRankService service;

    @GetMapping("/counties")
    public Result<?> getCountyRank(
            @RequestParam String state,
            @RequestParam Integer year,
            @RequestParam(defaultValue = "top") String order
    ) {
        return Result.ok(service.getCountyRank(state, year, order));
    }
}
