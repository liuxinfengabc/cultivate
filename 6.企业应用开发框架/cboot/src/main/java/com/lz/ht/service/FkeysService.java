package com.lz.ht.service;

import com.lz.ht.model.Fkeys;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface FkeysService {

    void add(Fkeys record);

    void updateById(Fkeys record);

    int deleteById(Long fkeysId);

    Fkeys findById(Long fkeysId);

    List<Fkeys> findAll();

    Fkeys findOne(Fkeys record);

    List<Fkeys> findList(Fkeys record);

    List<Fkeys> findPageList(PageModel<Fkeys> page, Fkeys fkeys) throws Exception;

    List<Fkeys> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(Fkeys fkeys) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

}
