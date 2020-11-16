package com.lz.ht.dao;

import com.lz.ht.model.Role;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface RoleMapper {

    void add(Role record);

    void updateById(Role record);

    int deleteById(Long RoleId);

    Role  findById(Long id);

    List<Role> findAll();

    List<Role> findList(Role record);

    long findCount(Role role);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<Role> findListByMapLimit(HashMap<String,Object> hashMap);

}
