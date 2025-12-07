package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface AmplitudeService {

    /**
     * 获取全国所有县的温度振幅
     */
    List<Map<String, Object>> getCountyAmplitudes();
}
