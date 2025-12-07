package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface RankingService {

    List<Map<String, Object>> topStates(Integer year, String order);

    List<Map<String, Object>> topCounties(Integer year, String order);
}
