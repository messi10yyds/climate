package com.basic.climate.service.impl;

import com.basic.climate.mapper.ExtremeMapper;
import com.basic.climate.service.ExtremeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExtremeServiceImpl implements ExtremeService {

    private final ExtremeMapper extremeMapper;

    @Override
    public Map<String, Object> getNationalExtreme() {
        Map<String, Object> result = new HashMap<>();
        result.put("max", extremeMapper.getNationalMax());
        result.put("min", extremeMapper.getNationalMin());
        return result;
    }

    @Override
    public Map<String, Object> getStateExtreme(String stateName) {
        Map<String, Object> result = new HashMap<>();
        result.put("max", extremeMapper.getStateMax(stateName));
        result.put("min", extremeMapper.getStateMin(stateName));
        return result;
    }

    @Override
    public Map<String, Object> getCountyExtreme(String countyName) {
        Map<String, Object> result = new HashMap<>();
        result.put("max", extremeMapper.getCountyMax(countyName));
        result.put("min", extremeMapper.getCountyMin(countyName));
        return result;
    }

    @Override
    public List<Map<String, Object>> getStateExtremeByYear(Integer year) {
        return extremeMapper.getStateExtremeByYear(year);
    }

    @Override
    public List<Map<String, Object>> getCountyExtremeByYear(String state, Integer year) {
        return extremeMapper.getCountyExtremeByYear(state, year);
    }

}
