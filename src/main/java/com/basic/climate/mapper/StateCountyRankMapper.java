package com.basic.climate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StateCountyRankMapper {

    /**
     * 查询某州某年的最高温 Top5 县
     */
    @Select("""
        SELECT 
            ci.county_name,
            ct.temp
        FROM county_temperature ct
        JOIN county_info ci 
            ON ct.fips = ci.fips
        JOIN state_info si
            ON ci.state_fips = si.state_fips
        WHERE 
            (LOWER(si.state_name) = LOWER(#{state})
            OR LOWER(si.stusab) = LOWER(#{state}))
            AND ct.year = #{year}
        ORDER BY ct.temp DESC
        LIMIT 5
    """)
    List<Map<String, Object>> getTop5Counties(
            @Param("state") String state,
            @Param("year") Integer year
    );

    /**
     * 查询某州某年的最低温 Bottom5 县
     */
    @Select("""
        SELECT 
            ci.county_name,
            ct.temp
        FROM county_temperature ct
        JOIN county_info ci 
            ON ct.fips = ci.fips
        JOIN state_info si
            ON ci.state_fips = si.state_fips
        WHERE 
            (LOWER(si.state_name) = LOWER(#{state})
            OR LOWER(si.stusab) = LOWER(#{state}))
            AND ct.year = #{year}
        ORDER BY ct.temp ASC
        LIMIT 5
    """)
    List<Map<String, Object>> getBottom5Counties(
            @Param("state") String state,
            @Param("year") Integer year
    );

    @Select("""
    SELECT 
        si.state_name,
        si.stusab,
        st.temp
    FROM state_temperature st
    JOIN state_info si
        ON st.state_name = si.state_name
    WHERE 
        (LOWER(si.state_name) = LOWER(#{state})
        OR LOWER(si.stusab) = LOWER(#{state}))
        AND st.year = #{year}
    LIMIT 1
""")
    Map<String, Object> getStateTemp(
            @Param("state") String state,
            @Param("year") Integer year
    );

}
