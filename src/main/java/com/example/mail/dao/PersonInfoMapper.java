package com.example.mail.dao;

import com.example.mail.entity.PersonInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonInfoMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(PersonInfo record);

    int insertSelective(PersonInfo record);

    PersonInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(PersonInfo record);

    int updateByPrimaryKey(PersonInfo record);

    int setEnableStatus(String name);
}