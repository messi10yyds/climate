package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/api/rank")
    public Result<?> getRanking(
            @RequestParam Integer year,
            @RequestParam String type,
            @RequestParam(required = false, defaultValue = "desc") String order
    ) {
        if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
            return Result.fail("order must be asc or desc");
        }

        if (type.equalsIgnoreCase("state")) {
            return Result.ok(rankingService.topStates(year, order));
        }

        if (type.equalsIgnoreCase("county")) {
            return Result.ok(rankingService.topCounties(year, order));
        }

        return Result.fail("type must be 'state' or 'county'");
    }
}
