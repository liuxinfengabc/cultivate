package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.RoleMapper;
import com.lz.ht.model.Role;
import com.lz.ht.service.RoleService;
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
public class RoleServiceImpl  implements  RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void add(Role record) {
        roleMapper.add(record);
    }

    @Override
    public void updateById(Role record) {
        roleMapper.updateById(record);
    }

    @Override
    public int deleteById(Long RoleId) {
        return  roleMapper.deleteById(RoleId);
    }

    @Override
    public Role findById(Long RoleId) {
        return  roleMapper.findById(RoleId);
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }


    @Override
    public Role findOne(Role record) {
        List<Role>  list = roleMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<Role> findList(Role record){
         return  roleMapper.findList(record);
    }

    @Override
    public List<Role> findPageList(PageModel<Role> page, Role role) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(role);
        List<Role> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<Role> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return roleMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(Role  role) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(role);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return roleMapper.findCountByMap(hashMap);
    }


 }
