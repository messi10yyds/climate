package com.basic.climate.mapper;

import com.basic.climate.entity.NationalTemperature;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NationalTemperatureMapper {

    @Select("SELECT * FROM national_temperature LIMIT 1")
    NationalTemperature findOne();
}