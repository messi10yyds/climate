package com.basic.climate.service.impl;

import com.basic.climate.mapper.StateCountyRankMapper;
import com.basic.climate.service.StateCountyRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StateCountyRankServiceImpl implements StateCountyRankService {

    private final StateCountyRankMapper mapper;

    @Override
    public List<Map<String, Object>> getCountyRank(String state, Integer year, String order) {

        // 查询县级 Top5 / Bottom5
        List<Map<String, Object>> raw;
        if ("bottom".equalsIgnoreCase(order)) {
            raw = mapper.getBottom5Counties(state, year);
        } else {
            raw = mapper.getTop5Counties(state, year);
        }

        // 查询州温度
        Map<String, Object> stateTemp = mapper.getStateTemp(state, year);

        // 构造返回结构
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("state", stateTemp.get("state_name"));
        result.put("stusab", stateTemp.get("stusab"));
        result.put("year", year);
        result.put("state_temp", stateTemp.get("temp"));
        result.put("data", raw);

        return List.of(result);
    }

}
