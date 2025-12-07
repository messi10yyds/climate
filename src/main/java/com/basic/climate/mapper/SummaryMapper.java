package com.basic.climate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SummaryMapper {

    @Select("SELECT COUNT(*) FROM state_info")
    int getStateCount();

    @Select("SELECT COUNT(*) FROM county_info")
    int getCountyCount();

    @Select("SELECT COUNT(*) FROM county_temperature")
    int countCountyTemperature();

    @Select("SELECT COUNT(*) FROM state_temperature")
    int countStateTemperature();

    @Select("SELECT COUNT(*) FROM national_temperature")
    int countNationalTemperature();
}