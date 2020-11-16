package com.lz.ht.dao;

import com.lz.ht.model.RoleResources;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface RoleResourcesMapper {

    void add(RoleResources record);

    void updateById(RoleResources record);

    int deleteById(Long RoleResourcesId);

    RoleResources  findById(Long id);

    List<RoleResources> findAll();

    List<RoleResources> findList(RoleResources record);

    long findCount(RoleResources roleResources);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<RoleResources> findListByMapLimit(HashMap<String,Object> hashMap);

}
