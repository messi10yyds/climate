package com.basic.climate.service.impl;

import com.basic.climate.mapper.SummaryMapper;
import com.basic.climate.service.SummaryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final SummaryMapper summaryMapper;

    public SummaryServiceImpl(SummaryMapper summaryMapper) {
        this.summaryMapper = summaryMapper;
    }

    @Override
    public Map<String, Object> getSummary() {
        Map<String, Object> result = new HashMap<>();

        // 基本数据
        result.put("yearStart", 1895);
        result.put("yearEnd", 2019);
        result.put("totalStates", summaryMapper.getStateCount());
        result.put("totalCounties", summaryMapper.getCountyCount());

        // 表行数
        List<Map<String, Object>> tables = new ArrayList<>();
        tables.add(createTable("county_temperature", summaryMapper.countCountyTemperature()));
        tables.add(createTable("state_temperature", summaryMapper.countStateTemperature()));
        tables.add(createTable("national_temperature", summaryMapper.countNationalTemperature()));
        tables.add(createTable("state_info", summaryMapper.getStateCount()));
        tables.add(createTable("county_info", summaryMapper.getCountyCount()));
        result.put("tables", tables);

        return result;
    }

    private Map<String, Object> createTable(String name, int rows) {
        Map<String, Object> map = new HashMap<>();
        map.put("table", name);
        map.put("rows", rows);
        return map;
    }
}