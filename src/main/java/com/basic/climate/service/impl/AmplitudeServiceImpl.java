package com.basic.climate.service.impl;

import com.basic.climate.mapper.AmplitudeMapper;
import com.basic.climate.service.AmplitudeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AmplitudeServiceImpl implements AmplitudeService {

    private final AmplitudeMapper amplitudeMapper;

    @Override
    public List<Map<String, Object>> getCountyAmplitudes() {
        return amplitudeMapper.getCountyAmplitudes();
    }
}
