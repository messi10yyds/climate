package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.service.AmplitudeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/amplitude")
@RequiredArgsConstructor
public class AmplitudeController {

    private final AmplitudeService amplitudeService;

    @GetMapping("/counties")
    public Result<?> getCountyAmplitude() {
        return Result.ok(amplitudeService.getCountyAmplitudes());
    }
}
