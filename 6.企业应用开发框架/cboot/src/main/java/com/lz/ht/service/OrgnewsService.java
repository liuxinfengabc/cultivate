package com.lz.ht.service;

import com.lz.ht.dto.OrgnewsDto;
import com.lz.ht.model.Orgnews;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface OrgnewsService {

    void add(Orgnews record);

    void updateById(Orgnews record);

    int deleteById(Long orgnewsId);

    Orgnews findById(Long orgnewsId);

    List<Orgnews> findAll();

    Orgnews findOne(Orgnews record);

    List<Orgnews> findList(Orgnews record);

    List<Orgnews> findPageList(PageModel<Orgnews> page, Orgnews orgnews) throws Exception;

    List<Orgnews> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(Orgnews orgnews) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

	List<Orgnews> findPageListByDto(PageModel<Orgnews> page, OrgnewsDto orgnewsDto) throws Exception;

	long findCountByDto(OrgnewsDto orgnewsDto) throws Exception;

	 

}
