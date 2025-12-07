package com.basic.climate.controller;

import com.basic.climate.common.Result;
import com.basic.climate.entity.NationalTemperature;
import com.basic.climate.mapper.NationalTemperatureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final NationalTemperatureMapper mapper;

    @GetMapping("/test")
    public Result<NationalTemperature> test() {
        NationalTemperature data = mapper.findOne();
        return Result.ok(data);
    }
}
