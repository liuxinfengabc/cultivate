package com.lz.ht.service;

import com.lz.ht.model.CostProject;
import com.lz.ht.model.User;
import com.lz.ht.page.PageModel;

import java.util.HashMap;
import java.util.List;

public interface CostProjectService {
    List<CostProject> findAll();

    CostProject findOne(CostProject record);

    List<CostProject> findList(CostProject record);

    List<CostProject> findPageList(PageModel<CostProject> page, CostProject costProject) throws Exception;

    List<CostProject> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(CostProject costProject) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);


    int deleteByPrimaryKey(Integer projectId);

    void insert(CostProject record);



    int insertSelective(CostProject record);

    CostProject selectByPrimaryKey(Integer projectId);

    int updateByPrimaryKeySelective(CostProject record);

    int updateByPrimaryKey(CostProject record);

    CostProject findById(Integer projectId);


}
