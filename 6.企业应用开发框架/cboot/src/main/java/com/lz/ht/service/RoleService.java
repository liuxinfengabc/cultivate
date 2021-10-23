package com.lz.ht.service;

import com.lz.ht.model.Role;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface RoleService {

    void add(Role record);

    void updateById(Role record);

    int deleteById(Long roleId);

    Role findById(Long roleId);

    List<Role> findAll();

    Role findOne(Role record);

    List<Role> findList(Role record);

    List<Role> findPageList(PageModel<Role> page, Role role) throws Exception;

    List<Role> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(Role role) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

}
