package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.UserRoleMapper;
import com.lz.ht.model.RoleResources;
import com.lz.ht.model.UserRole;
import com.lz.ht.service.UserRoleService;
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
public class UserRoleServiceImpl  implements  UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void add(UserRole record) {
        userRoleMapper.add(record);
    }

    @Override
    public void updateById(UserRole record) {
        userRoleMapper.updateById(record);
    }

    @Override
    public int deleteById(Long UserRoleId) {
        return  userRoleMapper.deleteById(UserRoleId);
    }

    @Override
    public UserRole findById(Long UserRoleId) {
        return  userRoleMapper.findById(UserRoleId);
    }

    @Override
    public List<UserRole> findAll() {
        return userRoleMapper.findAll();
    }


    @Override
    public UserRole findOne(UserRole record) {
        List<UserRole>  list = userRoleMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<UserRole> findList(UserRole record){
         return  userRoleMapper.findList(record);
    }

    @Override
    public List<UserRole> findPageList(PageModel<UserRole> page, UserRole userRole) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(userRole);
        List<UserRole> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<UserRole> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return userRoleMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(UserRole  userRole) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(userRole);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return userRoleMapper.findCountByMap(hashMap);
    }

    @Override
	public void updateRoleKeys(String[] roleKeysArr, Long userId) {
		 if (roleKeysArr == null || roleKeysArr.length==0) return;
		 UserRole record = new UserRole();
		 record.setUserId(userId);
		 List<UserRole> findList = findList(record);
		 for(UserRole r:findList) {
			 deleteById(r.getId());
		 }
		 for(int i=0;i<roleKeysArr.length;i++) {
			 UserRole record2 = new UserRole();
			 record2.setRoleKey(roleKeysArr[i]);
			 record2.setUserId(userId);
		     userRoleMapper.add(record2);
		 }
		
	}
 }
