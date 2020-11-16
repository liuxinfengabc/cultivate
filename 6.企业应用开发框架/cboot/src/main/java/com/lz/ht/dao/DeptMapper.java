package com.lz.ht.dao;

import com.lz.ht.model.Dept;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface DeptMapper {

    void add(Dept record);

    void updateById(Dept record);

    int deleteById(Long DeptId);

    Dept  findById(Long id);

    List<Dept> findAll();

    List<Dept> findList(Dept record);

    long findCount(Dept dept);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<Dept> findListByMapLimit(HashMap<String,Object> hashMap);

}
