package com.basic.climate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CompareMapper {

    @Select({
            "<script>",
            "SELECT",
            "   si.state_name,",
            "   si.stusab,",
            "   st.year,",
            "   st.temp",
            "FROM state_temperature st",
            "JOIN state_info si ON st.fips = si.state_fips",
            "WHERE",
            "   st.year = #{year}",
            "   AND (",
            "       LOWER(si.state_name) IN",
            "       <foreach collection='states' item='s' open='(' separator=',' close=')'>",
            "           LOWER(#{s})",
            "       </foreach>",
            "       OR LOWER(si.stusab) IN",
            "       <foreach collection='states' item='s' open='(' separator=',' close=')'>",
            "           LOWER(#{s})",
            "       </foreach>",
            "   )",
            "ORDER BY si.state_name ASC",
            "</script>"
    })
    List<Map<String, Object>> compareStates(
            @Param("states") List<String> states,
            @Param("year") Integer year
    );


    @Select({
            "<script>",
            "SELECT",
            "   ci.county_name,",
            "   ci.fips AS county_fips,",
            "   ct.year,",
            "   ct.temp",
            "FROM county_temperature ct",
            "JOIN county_info ci ON ct.fips = ci.fips",
            "WHERE",
            "   ct.year = #{year}",
            "   AND LOWER(ci.county_name) IN",
            "   <foreach collection='counties' item='c' open='(' separator=',' close=')'>",
            "       LOWER(CONCAT(#{c}, ' County'))",
            "   </foreach>",
            "ORDER BY ci.county_name ASC",
            "</script>"
    })
    List<Map<String, Object>> compareCounties(
            @Param("counties") List<String> counties,
            @Param("year") Integer year
    );

}
