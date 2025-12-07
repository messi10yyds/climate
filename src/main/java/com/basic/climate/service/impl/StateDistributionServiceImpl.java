package com.basic.climate.service.impl;

import com.basic.climate.mapper.StateDistributionMapper;
import com.basic.climate.service.StateDistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StateDistributionServiceImpl implements StateDistributionService {

    private final StateDistributionMapper mapper;

    @Override
    public List<Map<String, Object>> getStateDistribution(Integer year) {
        return mapper.getStateDistribution(year);
    }
}
