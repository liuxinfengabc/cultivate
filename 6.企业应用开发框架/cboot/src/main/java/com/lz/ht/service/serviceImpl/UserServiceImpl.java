package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.UserMapper;
import com.lz.ht.dao.UserRoleMapper;
import com.lz.ht.model.RoleResources;
import com.lz.ht.model.User;
import com.lz.ht.model.UserRole;
import com.lz.ht.service.UserService;
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
public class UserServiceImpl  implements  UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;
    
    
    @Override
    public void add(User record) {
        userMapper.add(record);
    }

    @Override
    public void updateById(User record) {
        userMapper.updateById(record);
    }

    @Override
    public int deleteById(Long UserId) {
        return  userMapper.deleteById(UserId);
    }

    @Override
    public User findById(Long UserId) {
        return  userMapper.findById(UserId);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }


    @Override
    public User findOne(User record) {
        List<User>  list = userMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<User> findList(User record){
         return  userMapper.findList(record);
    }

    @Override
    public List<User> findPageList(PageModel<User> page, User user) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(user);
        List<User> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<User> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return userMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(User  user) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(user);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return userMapper.findCountByMap(hashMap);
    }

	@Override
	public void deleteUserCascade(Long id) {
	    deleteById(id);
        UserRole record = new UserRole();
        record.setUserId(id);
        List<UserRole> findList = userRoleMapper.findList(record);
        for (UserRole u : findList) {
        	userRoleMapper.deleteById(u.getId());
		}
	}

	


 }
