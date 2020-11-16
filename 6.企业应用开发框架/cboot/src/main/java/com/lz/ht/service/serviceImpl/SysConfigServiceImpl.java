package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.SysConfigMapper;
import com.lz.ht.model.SysConfig;
import com.lz.ht.service.SysConfigService;
import com.lz.ht.page.PageModel;
import com.lz.ht.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.apache.ibatis.exceptions.TooManyResultsException;
import java.util.List;
import java.util.HashMap;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysConfigServiceImpl  implements  SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public void add(SysConfig record) {
        sysConfigMapper.add(record);
    }

    @Override
    public void updateById(SysConfig record) {
        sysConfigMapper.updateById(record);
    }

    @Override
    public int deleteById(Long SysConfigId) {
        return  sysConfigMapper.deleteById(SysConfigId);
    }

    @Override
    public SysConfig findById(Long SysConfigId) {
        return  sysConfigMapper.findById(SysConfigId);
    }

    @Override
    public List<SysConfig> findAll() {
        return sysConfigMapper.findAll();
    }


    @Override
    public SysConfig findOne(SysConfig record) {
        List<SysConfig>  list = sysConfigMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<SysConfig> findList(SysConfig record){
         return  sysConfigMapper.findList(record);
    }

    @Override
    public List<SysConfig> findPageList(PageModel<SysConfig> page, SysConfig sysConfig) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(sysConfig);
        List<SysConfig> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<SysConfig> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return sysConfigMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(SysConfig  sysConfig) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(sysConfig);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return sysConfigMapper.findCountByMap(hashMap);
    }


 }
