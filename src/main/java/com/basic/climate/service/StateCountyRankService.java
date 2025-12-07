package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface StateCountyRankService {

    /**
     * 查询某州某年的 Top5 / Bottom5 县
     * @param state 州名或缩写
     * @param year 年份
     * @param order 排序方式：top / bottom
     * @return 返回 List，每个元素为 { county_name, temp }
     */
    List<Map<String, Object>> getCountyRank(String state, Integer year, String order);
}
