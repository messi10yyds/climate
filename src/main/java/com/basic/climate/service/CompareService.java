package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface CompareService {

    List<Map<String, Object>> compareStates(List<String> states, Integer year);

    List<Map<String, Object>> compareCounties(List<String> counties, Integer year);
}
