package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface ExtremeService {

    Map<String, Object> getNationalExtreme();

    Map<String, Object> getStateExtreme(String stateName);

    Map<String, Object> getCountyExtreme(String countyName);

    List<Map<String, Object>> getStateExtremeByYear(Integer year);

    List<Map<String, Object>> getCountyExtremeByYear(String state, Integer year);

}
