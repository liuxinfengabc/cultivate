package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.ResourcesMapper;
import com.lz.ht.model.Resources;
import com.lz.ht.service.ResourcesService;
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
public class ResourcesServiceImpl  implements  ResourcesService {

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Override
    public void add(Resources record) {
        resourcesMapper.add(record);
    }

    @Override
    public void updateById(Resources record) {
        resourcesMapper.updateById(record);
    }

    @Override
    public int deleteById(Long ResourcesId) {
        return  resourcesMapper.deleteById(ResourcesId);
    }

    @Override
    public Resources findById(Long ResourcesId) {
        return  resourcesMapper.findById(ResourcesId);
    }

    @Override
    public List<Resources> findAll() {
        return resourcesMapper.findAll();
    }


    @Override
    public Resources findOne(Resources record) {
        List<Resources>  list = resourcesMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<Resources> findList(Resources record){
         return  resourcesMapper.findList(record);
    }

    @Override
    public List<Resources> findPageList(PageModel<Resources> page, Resources resources) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(resources);
        List<Resources> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<Resources> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return resourcesMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(Resources  resources) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(resources);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return resourcesMapper.findCountByMap(hashMap);
    }


 }
