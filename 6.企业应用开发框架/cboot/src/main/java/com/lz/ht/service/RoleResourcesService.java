package com.lz.ht.service;

import com.lz.ht.model.RoleResources;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface RoleResourcesService {

    void add(RoleResources record);

    void updateById(RoleResources record);

    int deleteById(Long roleResourcesId);

    RoleResources findById(Long roleResourcesId);

    List<RoleResources> findAll();

    RoleResources findOne(RoleResources record);

    List<RoleResources> findList(RoleResources record);

    List<RoleResources> findPageList(PageModel<RoleResources> page, RoleResources roleResources) throws Exception;

    List<RoleResources> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(RoleResources roleResources) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

	void updateResKeys(String[] resKeysArr, String roleKey);

}
