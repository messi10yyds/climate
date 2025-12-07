package com.basic.climate.service.impl;

import com.basic.climate.mapper.RankingMapper;
import com.basic.climate.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingMapper rankingMapper;

    @Override
    public List<Map<String, Object>> topStates(Integer year, String order) {
        return rankingMapper.topStates(year, order);
    }

    @Override
    public List<Map<String, Object>> topCounties(Integer year, String order) {
        return rankingMapper.topCounties(year, order);
    }
}
