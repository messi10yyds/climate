package com.basic.climate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface RankingMapper {

    @Select({
            "<script>",
            "SELECT",
            "   si.state_name,",
            "   si.stusab,",
            "   st.year,",
            "   st.temp",
            "FROM state_temperature st",
            "JOIN state_info si ON st.fips = si.state_fips",
            "WHERE st.year = #{year}",
            "ORDER BY st.temp",
            "   <choose>",
            "       <when test='order == \"asc\"'>ASC</when>",
            "       <otherwise>DESC</otherwise>",
            "   </choose>",
            "LIMIT 10",
            "</script>"
    })
    List<Map<String, Object>> topStates(
            @Param("year") Integer year,
            @Param("order") String order
    );

    @Select({
            "<script>",
            "SELECT",
            "   ci.county_name,",
            "   ci.fips AS county_fips,",
            "   si.state_name,",
            "   si.stusab,",
            "   ct.year,",
            "   ct.temp",
            "FROM county_temperature ct",
            "JOIN county_info ci ON ct.fips = ci.fips",
            "JOIN state_info si ON ci.state_fips = si.state_fips",
            "WHERE ct.year = #{year}",
            "ORDER BY ct.temp",
            "   <choose>",
            "       <when test='order == \"asc\"'>ASC</when>",
            "       <otherwise>DESC</otherwise>",
            "   </choose>",
            "LIMIT 10",
            "</script>"
    })
    List<Map<String, Object>> topCounties(
            @Param("year") Integer year,
            @Param("order") String order
    );
}
