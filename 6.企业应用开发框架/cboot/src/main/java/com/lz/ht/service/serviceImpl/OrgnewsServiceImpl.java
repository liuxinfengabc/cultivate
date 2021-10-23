package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.OrgnewsMapper;
import com.lz.ht.dto.OrgnewsDto;
import com.lz.ht.model.Orgnews;
import com.lz.ht.service.OrgnewsService;
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
public class OrgnewsServiceImpl  implements  OrgnewsService {

    @Autowired
    private OrgnewsMapper orgnewsMapper;

    @Override
    public void add(Orgnews record) {
        orgnewsMapper.add(record);
    }

    @Override
    public void updateById(Orgnews record) {
        orgnewsMapper.updateById(record);
    }

    @Override
    public int deleteById(Long OrgnewsId) {
        return  orgnewsMapper.deleteById(OrgnewsId);
    }

    @Override
    public Orgnews findById(Long OrgnewsId) {
        return  orgnewsMapper.findById(OrgnewsId);
    }

    @Override
    public List<Orgnews> findAll() {
        return orgnewsMapper.findAll();
    }


    @Override
    public Orgnews findOne(Orgnews record) {
        List<Orgnews>  list = orgnewsMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<Orgnews> findList(Orgnews record){
         return  orgnewsMapper.findList(record);
    }

    @Override
    public List<Orgnews> findPageList(PageModel<Orgnews> page, Orgnews orgnews) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(orgnews);
        List<Orgnews> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<Orgnews> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return orgnewsMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(Orgnews  orgnews) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(orgnews);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return orgnewsMapper.findCountByMap(hashMap);
    }

	@Override
	public List<Orgnews> findPageListByDto(PageModel<Orgnews> page, OrgnewsDto orgnewsDto) throws Exception { 
		HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(orgnewsDto);
	    hashMap.put("msFirst",page.getMsFirst());
        hashMap.put("msLast",page.getMsLast());
		return orgnewsMapper.findPageListByDto(hashMap);
	}

	@Override
	public long findCountByDto(OrgnewsDto orgnewsDto) throws Exception {
		HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(orgnewsDto); 
		return orgnewsMapper.findCountByDto(hashMap);
	}


 }
