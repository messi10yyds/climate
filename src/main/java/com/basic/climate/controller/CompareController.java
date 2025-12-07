package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.service.CompareService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompareController {

    private final CompareService compareService;

    @GetMapping("/api/compare")
    public Result<?> compareRegions(
            @RequestParam String type,
            @RequestParam Integer year,
            @RequestParam String regions
    ) {
        // 将 "AL,CA,TX" / "Autauga,Baldwin" 解析为 List
        List<String> list = Arrays.stream(regions.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        if (list.isEmpty()) {
            return Result.fail("regions cannot be empty");
        }

        // 对 state 进行多州比较
        if (type.equalsIgnoreCase("state")) {
            return Result.ok(compareService.compareStates(list, year));
        }

        // 对 county 进行多个县比较
        if (type.equalsIgnoreCase("county")) {
            return Result.ok(compareService.compareCounties(list, year));
        }

        return Result.fail("Invalid type. Must be 'state' or 'county'.");
    }
}
