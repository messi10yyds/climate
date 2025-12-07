package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface StateDistributionService {
    List<Map<String, Object>> getStateDistribution(Integer year);
}
