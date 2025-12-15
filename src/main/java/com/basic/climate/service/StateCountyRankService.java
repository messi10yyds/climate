package com.basic.climate.service;

import java.util.List;
import java.util.Map;

public interface StateCountyRankService {


    List<Map<String, Object>> getCountyRank(String state, Integer year, String order);
}
