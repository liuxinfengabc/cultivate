package com.lz.ht.service;

import com.lz.ht.model.Dept;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface DeptService {

    void add(Dept record);

    void updateById(Dept record);

    int deleteById(Long deptId);

    Dept findById(Long deptId);

    List<Dept> findAll();

    Dept findOne(Dept record);

    List<Dept> findList(Dept record);

    List<Dept> findPageList(PageModel<Dept> page, Dept dept) throws Exception;

    List<Dept> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(Dept dept) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

}
