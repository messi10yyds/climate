package com.basic.climate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StateDistributionMapper {

    @Select("""
            SELECT state_name, temp 
            FROM state_temperature
            WHERE year = #{year}
            ORDER BY state_name ASC
            """)
    List<Map<String, Object>> getStateDistribution(@Param("year") Integer year);
}
