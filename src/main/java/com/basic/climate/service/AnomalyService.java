package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface AnomalyService {

    /** 异常州 + 同年异常县（树结构） */
    List<Map<String, Object>> getStateAnomalyTree();

    /** 所有异常县（扁平列表） */
    List<Map<String, Object>> getCountyAnomalies();
}
