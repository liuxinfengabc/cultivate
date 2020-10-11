package com.tule.dao;

import com.tule.dao.TbUser;

public interface TbUserDao {
    int deleteByPrimaryKey(String userid);

    int insert(TbUser record);

    int insertSelective(TbUser record);

    TbUser selectByPrimaryKey(String userid);

    int updateByPrimaryKeySelective(TbUser record);

    int updateByPrimaryKey(TbUser record);
}