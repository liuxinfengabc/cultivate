package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.OrgcommentMapper;
import com.lz.ht.dto.OrgcommentDto;
import com.lz.ht.model.Orgcomment;
import com.lz.ht.service.OrgcommentService;
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
public class OrgcommentServiceImpl  implements  OrgcommentService {

    @Autowired
    private OrgcommentMapper orgcommentMapper;

    @Override
    public void add(Orgcomment record) {
        orgcommentMapper.add(record);
    }

    @Override
    public void updateById(Orgcomment record) {
        orgcommentMapper.updateById(record);
    }

    @Override
    public int deleteById(Long OrgcommentId) {
        return  orgcommentMapper.deleteById(OrgcommentId);
    }

    @Override
    public Orgcomment findById(Long OrgcommentId) {
        return  orgcommentMapper.findById(OrgcommentId);
    }

    @Override
    public List<Orgcomment> findAll() {
        return orgcommentMapper.findAll();
    }


    @Override
    public Orgcomment findOne(Orgcomment record) {
        List<Orgcomment>  list = orgcommentMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<Orgcomment> findList(Orgcomment record){
         return  orgcommentMapper.findList(record);
    }

    @Override
    public List<Orgcomment> findPageList(PageModel<Orgcomment> page, Orgcomment orgcomment) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(orgcomment);
        List<Orgcomment> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<Orgcomment> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return orgcommentMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(Orgcomment  orgcomment) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(orgcomment);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return orgcommentMapper.findCountByMap(hashMap);
    }

	@Override
	public List<OrgcommentDto> findPageListByDto(PageModel<OrgcommentDto> page, OrgcommentDto orgcommentDto) throws Exception {
		HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(orgcommentDto);
	    hashMap.put("msFirst",page.getMsFirst());
	    hashMap.put("msLast",page.getMsLast());
		return orgcommentMapper.findPageListByDto(hashMap);
	}

	@Override
	public long findCountByDto(OrgcommentDto orgcommentDto) throws Exception {
		HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(orgcommentDto); 
		return orgcommentMapper.findCountByDto(hashMap);
	}


 }
