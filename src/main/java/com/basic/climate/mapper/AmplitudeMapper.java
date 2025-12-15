package com.basic.climate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AmplitudeMapper {


    @Select("""
        SELECT
            ci.county_name,
            si.state_name,

            t1.max_temp,
            t1.max_year,

            t2.min_temp,
            t2.min_year,

            (t1.max_temp - t2.min_temp) AS amplitude

        FROM county_info ci

        JOIN state_info si 
            ON ci.state_fips = si.state_fips

        JOIN (
            SELECT 
                fips,
                MAX(temp) AS max_temp,
                SUBSTRING_INDEX(
                    GROUP_CONCAT(year ORDER BY temp DESC), ',', 1
                ) AS max_year
            FROM county_temperature
            GROUP BY fips
        ) t1 ON t1.fips = ci.fips

        JOIN (
            SELECT 
                fips,
                MIN(temp) AS min_temp,
                SUBSTRING_INDEX(
                    GROUP_CONCAT(year ORDER BY temp ASC), ',', 1
                ) AS min_year
            FROM county_temperature
            GROUP BY fips
        ) t2 ON t2.fips = ci.fips

        ORDER BY amplitude DESC;
    """)
    List<Map<String, Object>> getCountyAmplitudes();
}
