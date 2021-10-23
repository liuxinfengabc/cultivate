package com.lz.ht.dao;

import com.lz.ht.model.SysConfig;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface SysConfigMapper {

    void add(SysConfig record);

    void updateById(SysConfig record);

    int deleteById(Long SysConfigId);

    SysConfig  findById(Long id);

    List<SysConfig> findAll();

    List<SysConfig> findList(SysConfig record);

    long findCount(SysConfig sysConfig);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<SysConfig> findListByMapLimit(HashMap<String,Object> hashMap);

}
