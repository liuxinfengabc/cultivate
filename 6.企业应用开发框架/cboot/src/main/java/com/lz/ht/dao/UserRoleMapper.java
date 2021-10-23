package com.lz.ht.dao;

import com.lz.ht.model.UserRole;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface UserRoleMapper {

    void add(UserRole record);

    void updateById(UserRole record);

    int deleteById(Long UserRoleId);

    UserRole  findById(Long id);

    List<UserRole> findAll();

    List<UserRole> findList(UserRole record);

    long findCount(UserRole userRole);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<UserRole> findListByMapLimit(HashMap<String,Object> hashMap);

}
