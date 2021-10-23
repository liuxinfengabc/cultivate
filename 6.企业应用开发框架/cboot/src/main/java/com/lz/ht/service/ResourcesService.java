package com.lz.ht.service;

import com.lz.ht.model.Resources;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface ResourcesService {

    void add(Resources record);

    void updateById(Resources record);

    int deleteById(Long resourcesId);

    Resources findById(Long resourcesId);

    List<Resources> findAll();

    Resources findOne(Resources record);

    List<Resources> findList(Resources record);

    List<Resources> findPageList(PageModel<Resources> page, Resources resources) throws Exception;

    List<Resources> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(Resources resources) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

}
