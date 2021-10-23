package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.FkeysMapper;
import com.lz.ht.model.Fkeys;
import com.lz.ht.service.FkeysService;
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
public class FkeysServiceImpl  implements  FkeysService {

    @Autowired
    private FkeysMapper fkeysMapper;

    @Override
    public void add(Fkeys record) {
        fkeysMapper.add(record);
    }

    @Override
    public void updateById(Fkeys record) {
        fkeysMapper.updateById(record);
    }

    @Override
    public int deleteById(Long FkeysId) {
        return  fkeysMapper.deleteById(FkeysId);
    }

    @Override
    public Fkeys findById(Long FkeysId) {
        return  fkeysMapper.findById(FkeysId);
    }

    @Override
    public List<Fkeys> findAll() {
        return fkeysMapper.findAll();
    }


    @Override
    public Fkeys findOne(Fkeys record) {
        List<Fkeys>  list = fkeysMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<Fkeys> findList(Fkeys record){
         return  fkeysMapper.findList(record);
    }

    @Override
    public List<Fkeys> findPageList(PageModel<Fkeys> page, Fkeys fkeys) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(fkeys);
        List<Fkeys> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<Fkeys> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return fkeysMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(Fkeys  fkeys) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(fkeys);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return fkeysMapper.findCountByMap(hashMap);
    }


 }
