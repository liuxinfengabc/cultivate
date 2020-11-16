package com.lz.ht.service;

import com.lz.ht.model.UserRole;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface UserRoleService {

    void add(UserRole record);

    void updateById(UserRole record);

    int deleteById(Long userRoleId);

    UserRole findById(Long userRoleId);

    List<UserRole> findAll();

    UserRole findOne(UserRole record);

    List<UserRole> findList(UserRole record);

    List<UserRole> findPageList(PageModel<UserRole> page, UserRole userRole) throws Exception;

    List<UserRole> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(UserRole userRole) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

    void updateRoleKeys(String[] roleKeysArr, Long userId);
}
