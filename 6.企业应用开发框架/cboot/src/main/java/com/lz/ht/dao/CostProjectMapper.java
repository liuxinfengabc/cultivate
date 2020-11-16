package com.lz.ht.dao;

import com.lz.ht.model.CostProject;
import com.lz.ht.model.Customer;
import com.lz.ht.model.User;

import java.util.HashMap;
import java.util.List;

public interface CostProjectMapper {
    int deleteByPrimaryKey(Integer projectId);

    int insert(CostProject record);

    int insertSelective(CostProject record);

    CostProject selectByPrimaryKey(Integer projectId);

    int updateByPrimaryKeySelective(CostProject record);

    int updateByPrimaryKey(CostProject record);

    List<CostProject> findAll();

    CostProject findById(Integer projectId);

//    CostProjectMapper findById(Long id);
//
//    List<User> findAll();
//
//    List<User> findList(User record);
//
//    long findCount(User user);
//
//    long findCountByMap(HashMap<String,Object> hashMap);

//    List<CostProject> findListByMapLimit(HashMap<String,Object> hashMap);

}