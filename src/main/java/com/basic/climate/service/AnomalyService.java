package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface AnomalyService {

    List<Map<String, Object>> getStateAnomalyTree();

    List<Map<String, Object>> getCountyAnomalies();
}
