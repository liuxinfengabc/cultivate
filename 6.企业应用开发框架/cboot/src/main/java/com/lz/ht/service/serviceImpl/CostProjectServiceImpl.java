package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.CostProjectMapper;
import com.lz.ht.dao.UserMapper;
import com.lz.ht.model.CostProject;
import com.lz.ht.model.User;
import com.lz.ht.model.UserRole;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.CostProjectService;
import com.lz.ht.util.ToolKit;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CostProjectServiceImpl implements CostProjectService {
    @Autowired
    private CostProjectMapper costProjectMapper;

    @Override
    public List<CostProject> findAll() {
        return null;
    }

    @Override
    public CostProject findOne(CostProject record) {
        return null;
    }

    @Override
    public List<CostProject> findList(CostProject record) {
        return null;
    }

    @Override
    public List<CostProject> findPageList(PageModel<CostProject> page, CostProject costProject) throws Exception {
        return null;
    }

    @Override
    public List<CostProject> findListByMapLimit(HashMap<String, Object> hashMap, long first, long last) {
        return null;
    }

    @Override
    public long findCount(CostProject costProject) throws Exception {
        return 0;
    }

    @Override
    public long findCountByMap(HashMap<String, Object> hashMap) {
        return 0;
    }

    //    @Override
//    public void deleteUserCascade(Long id) {
//        deleteById(id);
//        UserRole record = new UserRole();
//        record.setUserId(id);
//        List<UserRole> findList = userRoleMapper.findList(record);
//        for (UserRole u : findList) {
//            userRoleMapper.deleteById(u.getId());
//        }
//    }
    @Override
    public int deleteByPrimaryKey(Integer projectId){
        return costProjectMapper.deleteByPrimaryKey(projectId);
    }

    @Override
    public void insert(CostProject record) {
        costProjectMapper.insert(record);
    }

    @Override
    public int insertSelective(CostProject record) {
        return 0;
    }

    @Override
    public CostProject selectByPrimaryKey(Integer projectId) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(CostProject record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(CostProject record) {
        return costProjectMapper.updateByPrimaryKey(record);
    }

    @Override
    public CostProject findById(Integer projectId) {
         return costProjectMapper.findById(projectId);
    }


//    public void add(CostProject record) {
//        costProjectMapper.insert(record);
//    }


//    @Override
//    public User findOne(User record) {
//        List<User>  list = costProjectMapper.findList(record);
//        if ((list!=null)&&(list.size() == 1)) {
//            return list.get(0);
//        } else if ((list!=null)&&(list.size() > 1)) {
//            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
//        } else {
//            return null;
//        }
//    }



}
