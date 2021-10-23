package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.RoleResourcesMapper;
import com.lz.ht.model.RoleResources;
import com.lz.ht.service.RoleResourcesService;
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
public class RoleResourcesServiceImpl  implements  RoleResourcesService {

    @Autowired
    private RoleResourcesMapper roleResourcesMapper;

    @Override
    public void add(RoleResources record) {
        roleResourcesMapper.add(record);
    }

    @Override
    public void updateById(RoleResources record) {
        roleResourcesMapper.updateById(record);
    }

    @Override
    public int deleteById(Long RoleResourcesId) {
        return  roleResourcesMapper.deleteById(RoleResourcesId);
    }

    @Override
    public RoleResources findById(Long RoleResourcesId) {
        return  roleResourcesMapper.findById(RoleResourcesId);
    }

    @Override
    public List<RoleResources> findAll() {
        return roleResourcesMapper.findAll();
    }


    @Override
    public RoleResources findOne(RoleResources record) {
        List<RoleResources>  list = roleResourcesMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<RoleResources> findList(RoleResources record){
         return  roleResourcesMapper.findList(record);
    }

    @Override
    public List<RoleResources> findPageList(PageModel<RoleResources> page, RoleResources roleResources) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(roleResources);
        List<RoleResources> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<RoleResources> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return roleResourcesMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(RoleResources  roleResources) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(roleResources);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return roleResourcesMapper.findCountByMap(hashMap);
    }

	@Override
	public void updateResKeys(String[] resKeysArr, String roleKey) {
		 if (resKeysArr == null || resKeysArr.length==0) return;
		 RoleResources record = new RoleResources();
		 record.setRoleKey(roleKey);
		 List<RoleResources> findList = findList(record);
		 for(RoleResources r:findList) {
			 deleteById(r.getId());
		 }
		 for(int i=0;i<resKeysArr.length;i++) {
			 RoleResources record2 = new RoleResources();
			 record2.setResKey(resKeysArr[i]);
			 record2.setRoleKey(roleKey);
			 roleResourcesMapper.add(record2);
		 }
		
	}


 }
