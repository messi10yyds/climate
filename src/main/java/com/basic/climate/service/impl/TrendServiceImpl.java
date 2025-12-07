package com.basic.climate.service.impl;

import com.basic.climate.mapper.TrendMapper;
import com.basic.climate.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrendServiceImpl implements TrendService {

    private final TrendMapper trendMapper;

    @Override
    public List<Map<String, Object>> getNationalTrend() {
        return trendMapper.getNationalTrend();
    }

    @Override
    public List<Map<String, Object>> getStateTrend(String stateName) {
        return trendMapper.getStateTrend(stateName);
    }

    @Override
    public List<Map<String, Object>> getCountyTrend(String countyName) {
        return trendMapper.getCountyTrend(countyName);
    }
}
