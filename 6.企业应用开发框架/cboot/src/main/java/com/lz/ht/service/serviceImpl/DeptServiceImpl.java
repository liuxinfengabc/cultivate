package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.DeptMapper;
import com.lz.ht.model.Dept;
import com.lz.ht.service.DeptService;
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
public class DeptServiceImpl  implements  DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public void add(Dept record) {
        deptMapper.add(record);
    }

    @Override
    public void updateById(Dept record) {
        deptMapper.updateById(record);
    }

    @Override
    public int deleteById(Long DeptId) {
        return  deptMapper.deleteById(DeptId);
    }

    @Override
    public Dept findById(Long DeptId) {
        return  deptMapper.findById(DeptId);
    }

    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }


    @Override
    public Dept findOne(Dept record) {
        List<Dept>  list = deptMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<Dept> findList(Dept record){
         return  deptMapper.findList(record);
    }

    @Override
    public List<Dept> findPageList(PageModel<Dept> page, Dept dept) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(dept);
        List<Dept> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<Dept> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return deptMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(Dept  dept) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(dept);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return deptMapper.findCountByMap(hashMap);
    }


 }
