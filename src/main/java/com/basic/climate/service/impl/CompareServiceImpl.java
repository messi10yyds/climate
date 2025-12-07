package com.basic.climate.service.impl;

import com.basic.climate.mapper.CompareMapper;
import com.basic.climate.service.CompareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompareServiceImpl implements CompareService {

    private final CompareMapper compareMapper;

    @Override
    public List<Map<String, Object>> compareStates(List<String> states, Integer year) {
        return compareMapper.compareStates(states, year);
    }

    @Override
    public List<Map<String, Object>> compareCounties(List<String> counties, Integer year) {
        return compareMapper.compareCounties(counties, year);
    }
}
