package com.basic.climate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TrendMapper {

    // 1. 全国趋势
    @Select("SELECT year, temp, tempc FROM national_temperature ORDER BY year ASC")
    List<Map<String, Object>> getNationalTrend();

    // 2. 州趋势（按州名）
    @Select("""
        SELECT st.year, st.temp, st.tempc
        FROM state_temperature st
        JOIN state_info si
            ON st.fips = si.state_fips
        WHERE 
            LOWER(si.state_name) = LOWER(#{state})
            OR LOWER(si.stusab) = LOWER(#{state})
        ORDER BY st.year ASC
        """)
    List<Map<String, Object>> getStateTrend(@Param("state") String state);


    // 3. 县趋势（按县名）
    @Select("""
            SELECT year, temp, tempc
            FROM county_temperature 
            WHERE LOWER(county_name) LIKE LOWER(CONCAT(#{countyName}, '%'))
            ORDER BY year ASC
            """)
    List<Map<String, Object>> getCountyTrend(@Param("countyName") String countyName);
}
