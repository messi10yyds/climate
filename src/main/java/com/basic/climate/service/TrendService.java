package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface TrendService {

    List<Map<String, Object>> getNationalTrend();

    List<Map<String, Object>> getStateTrend(String stateName);

    List<Map<String, Object>> getCountyTrend(String countyName);
}
