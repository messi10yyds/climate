package com.basic.climate.service.impl;

import com.basic.climate.mapper.AnomalyMapper;
import com.basic.climate.service.AnomalyService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnomalyServiceImpl implements AnomalyService {

    private final AnomalyMapper anomalyMapper;
    private static final double THRESHOLD = 3.6;

    public AnomalyServiceImpl(AnomalyMapper anomalyMapper) {
        this.anomalyMapper = anomalyMapper;
    }

    /** 删除 state_fips、county_fips 字段 */
    private void removeFips(Map<String, Object> row) {
        row.remove("state_fips");
        row.remove("county_fips");
    }

    /** 1. 异常州 + 同年异常县（树结构） */
    @Override
    public List<Map<String, Object>> getStateAnomalyTree() {
        List<Map<String, Object>> stateRows = anomalyMapper.getAllStateSeries();
        List<Map<String, Object>> countyRows = anomalyMapper.getAllCountySeries();

        Map<String, List<Map<String, Object>>> stateMap = new LinkedHashMap<>();
        for (Map<String, Object> row : stateRows) {
            stateMap.computeIfAbsent(
                    (String) row.get("state_fips"),
                    k -> new ArrayList<>()
            ).add(row);
        }

        Map<String, List<Map<String, Object>>> countyMap = new LinkedHashMap<>();
        for (Map<String, Object> row : countyRows) {
            countyMap.computeIfAbsent(
                    (String) row.get("county_fips"),
                    k -> new ArrayList<>()
            ).add(row);
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (List<Map<String, Object>> srows : stateMap.values()) {

            double meanState = calcMean(srows, "temp");

            for (Map<String, Object> srow : srows) {

                double temp = ((Number) srow.get("temp")).doubleValue();
                double deviation = temp - meanState;

                if (Math.abs(deviation) >= THRESHOLD) {

                    Map<String, Object> stateItem = new LinkedHashMap<>(srow);
                    removeFips(stateItem); // 🔥 删除 fips

                    stateItem.put("mean", meanState);
                    stateItem.put("deviation", deviation);
                    stateItem.put("anomaly", deviation > 0 ? "HIGH" : "LOW");

                    Integer year = (Integer) srow.get("year");
                    String stateFips = (String) srow.get("state_fips");

                    List<Map<String, Object>> abnormalCounties = new ArrayList<>();

                    for (List<Map<String, Object>> crows : countyMap.values()) {
                        double meanCounty = calcMean(crows, "temp");

                        for (Map<String, Object> crow : crows) {

                            if (!Objects.equals(crow.get("year"), year)) continue;
                            if (!Objects.equals(crow.get("state_fips"), stateFips)) continue;

                            double ctemp = ((Number) crow.get("temp")).doubleValue();
                            double cdev = ctemp - meanCounty;

                            if (Math.abs(cdev) >= THRESHOLD) {
                                Map<String, Object> citem = new LinkedHashMap<>();

                                citem.put("county_name", crow.get("county_name"));
                                citem.put("temp", crow.get("temp"));
                                citem.put("mean", meanCounty);
                                citem.put("deviation", cdev);
                                citem.put("anomaly", cdev > 0 ? "HIGH" : "LOW");
                                abnormalCounties.add(citem);
                            }
                        }
                    }

                    // 对异常县按 deviation 绝对值从大到小排序
                    abnormalCounties.sort((a, b) -> {
                        double ad = Math.abs(((Number) a.get("deviation")).doubleValue());
                        double bd = Math.abs(((Number) b.get("deviation")).doubleValue());
                        return Double.compare(bd, ad); // 降序
                    });

                    // 只保留前 3 个
                    if (abnormalCounties.size() > 3) {
                        abnormalCounties = abnormalCounties.subList(0, 3);
                    }

                    stateItem.put("counties", abnormalCounties);
                    result.add(stateItem);
                }
            }
        }

        result.sort(Comparator.comparingInt(o -> (Integer) o.get("year")));
        return result;
    }

    /** 2. 所有异常县（扁平列表） */
    @Override
    public List<Map<String, Object>> getCountyAnomalies() {
        List<Map<String, Object>> rows = anomalyMapper.getAllCountySeries();

        Map<String, List<Map<String, Object>>> byCounty = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            byCounty.computeIfAbsent(
                    (String) row.get("county_fips"),
                    k -> new ArrayList<>()
            ).add(row);
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (List<Map<String, Object>> countyRows : byCounty.values()) {
            double mean = calcMean(countyRows, "temp");

            for (Map<String, Object> row : countyRows) {
                double tempV = ((Number) row.get("temp")).doubleValue();
                double deviation = tempV - mean;

                if (Math.abs(deviation) >= THRESHOLD) {
                    Map<String, Object> item = new LinkedHashMap<>(row);
                    removeFips(item); // 🔥 删除 fips

                    item.put("mean", mean);
                    item.put("deviation", deviation);
                    item.put("anomaly", deviation > 0 ? "HIGH" : "LOW");
                    result.add(item);
                }
            }
        }

        result.sort(Comparator.comparingInt(o -> (Integer) o.get("year")));
        return result;
    }

    private double calcMean(List<Map<String, Object>> rows, String field) {
        double sum = 0;
        for (Map<String, Object> row : rows) {
            sum += ((Number) row.get(field)).doubleValue();
        }
        return rows.isEmpty() ? 0 : sum / rows.size();
    }
}
