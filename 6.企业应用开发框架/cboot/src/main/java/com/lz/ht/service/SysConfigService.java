package com.lz.ht.service;

import com.lz.ht.model.SysConfig;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface SysConfigService {

    void add(SysConfig record);

    void updateById(SysConfig record);

    int deleteById(Long sysConfigId);

    SysConfig findById(Long sysConfigId);

    List<SysConfig> findAll();

    SysConfig findOne(SysConfig record);

    List<SysConfig> findList(SysConfig record);

    List<SysConfig> findPageList(PageModel<SysConfig> page, SysConfig sysConfig) throws Exception;

    List<SysConfig> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(SysConfig sysConfig) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

}
