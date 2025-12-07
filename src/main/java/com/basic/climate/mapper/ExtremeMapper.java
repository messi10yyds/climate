package com.basic.climate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExtremeMapper {

    // ======================
    //   全国 (national)
    // ======================

    @Select("""
            SELECT year, temp
            FROM national_temperature
            ORDER BY temp DESC
            LIMIT 1
            """)
    Map<String, Object> getNationalMax();

    @Select("""
            SELECT year, temp
            FROM national_temperature
            ORDER BY temp ASC
            LIMIT 1
            """)
    Map<String, Object> getNationalMin();


    // ======================
    //   州 (state) — 忽略大小写
    // ======================

    @Select("""
        SELECT st.year, st.temp
        FROM state_temperature st
        JOIN state_info si
            ON st.fips = si.state_fips
        WHERE 
            LOWER(si.state_name) = LOWER(#{state})
            OR LOWER(si.stusab) = LOWER(#{state})
        ORDER BY st.temp DESC
        LIMIT 1
        """)
    Map<String, Object> getStateMax(@Param("state") String stateName);

    @Select("""
        SELECT st.year, st.temp
        FROM state_temperature st
        JOIN state_info si
            ON st.fips = si.state_fips
        WHERE 
            LOWER(si.state_name) = LOWER(#{state})
            OR LOWER(si.stusab) = LOWER(#{state})
        ORDER BY st.temp ASC
        LIMIT 1
        """)
    Map<String, Object> getStateMin(@Param("state") String stateName);


    // ======================
    //   县 (county) — 忽略大小写 + 支持输入“Autauga”匹配“Autauga County”
    // ======================

    @Select("""
            SELECT year, temp
            FROM county_temperature
            WHERE LOWER(county_name) LIKE LOWER(CONCAT(#{countyName}, '%'))
            ORDER BY temp DESC
            LIMIT 1
            """)
    Map<String, Object> getCountyMax(@Param("countyName") String countyName);

    @Select("""
            SELECT year, temp
            FROM county_temperature
            WHERE LOWER(county_name) LIKE LOWER(CONCAT(#{countyName}, '%'))
            ORDER BY temp ASC
            LIMIT 1
            """)
    Map<String, Object> getCountyMin(@Param("countyName") String countyName);

    @Select("""
    SELECT 
        si.state_name,
        si.stusab,
        si.state_fips,
        MAX(st.temp) AS max_temp,
        MIN(st.temp) AS min_temp
    FROM state_temperature st
    JOIN state_info si
        ON st.fips = si.state_fips
    WHERE st.year = #{year}
    GROUP BY si.state_name, si.stusab, si.state_fips
    ORDER BY si.state_name ASC
    """)
    List<Map<String, Object>> getStateExtremeByYear(@Param("year") Integer year);

    @Select("""
    SELECT
        ci.county_name,
        ci.fips AS county_fips,
        MAX(ct.temp) AS max_temp,
        MIN(ct.temp) AS min_temp
    FROM county_temperature ct
    JOIN county_info ci
        ON ct.fips = ci.fips
    JOIN state_info si
        ON ci.state_fips = si.state_fips
    WHERE 
        (LOWER(si.state_name) = LOWER(#{state})
         OR LOWER(si.stusab) = LOWER(#{state}))
      AND ct.year = #{year}
    GROUP BY ci.county_name, ci.fips
    ORDER BY ci.county_name ASC
    """)
    List<Map<String, Object>> getCountyExtremeByYear(
            @Param("state") String state,
            @Param("year") Integer year);

}
