package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.UserExtMapper;
import com.lz.ht.model.UserExt;
import com.lz.ht.service.UserExtService;
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
public class UserExtServiceImpl  implements  UserExtService {

    @Autowired
    private UserExtMapper userExtMapper;

    @Override
    public void add(UserExt record) {
        userExtMapper.add(record);
    }

    @Override
    public void updateById(UserExt record) {
        userExtMapper.updateById(record);
    }

    @Override
    public int deleteById(Long UserExtId) {
        return  userExtMapper.deleteById(UserExtId);
    }

    @Override
    public UserExt findById(Long UserExtId) {
        return  userExtMapper.findById(UserExtId);
    }

    @Override
    public List<UserExt> findAll() {
        return userExtMapper.findAll();
    }


    @Override
    public UserExt findOne(UserExt record) {
        List<UserExt>  list = userExtMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<UserExt> findList(UserExt record){
         return  userExtMapper.findList(record);
    }

    @Override
    public List<UserExt> findPageList(PageModel<UserExt> page, UserExt userExt) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(userExt);
        List<UserExt> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<UserExt> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return userExtMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(UserExt  userExt) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(userExt);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return userExtMapper.findCountByMap(hashMap);
    }


 }
