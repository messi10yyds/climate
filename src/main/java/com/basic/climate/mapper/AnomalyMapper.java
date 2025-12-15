package com.basic.climate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnomalyMapper {

    @Select("""
        SELECT 
            st.year,
            st.temp,
            si.state_name,
            si.stusab,
            si.state_fips
        FROM state_temperature st
        JOIN state_info si ON st.fips = si.state_fips
        ORDER BY si.state_name, st.year
        """)
    List<Map<String, Object>> getAllStateSeries();

    @Select("""
        SELECT
            ct.year,
            ct.temp,
            ci.county_name,
            ci.fips AS county_fips,
            si.state_name,
            si.stusab,
            si.state_fips
        FROM county_temperature ct
        JOIN county_info ci ON ct.fips = ci.fips
        JOIN state_info si ON ci.state_fips = si.state_fips
        ORDER BY si.state_name, ci.county_name, ct.year
        """)
    List<Map<String, Object>> getAllCountySeries();

    @Select("""
        SELECT
            ct.year,
            ct.temp,
            ci.county_name,
            ci.fips AS county_fips,
            si.state_name,
            si.stusab,
            si.state_fips
        FROM county_temperature ct
        JOIN county_info ci ON ct.fips = ci.fips
        JOIN state_info si ON ci.state_fips = si.state_fips
        WHERE 
            LOWER(si.state_name) = LOWER(#{state})
            OR LOWER(si.stusab) = LOWER(#{state})
        ORDER BY ci.county_name, ct.year
        """)
    List<Map<String, Object>> getStateCountySeries(@Param("state") String state);
}
