package com.lz.ht.dao;

import com.lz.ht.model.Orgnews;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface OrgnewsMapper {

    void add(Orgnews record);

    void updateById(Orgnews record);

    int deleteById(Long OrgnewsId);

    Orgnews  findById(Long id);

    List<Orgnews> findAll();

    List<Orgnews> findList(Orgnews record);

    long findCount(Orgnews orgnews);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<Orgnews> findListByMapLimit(HashMap<String,Object> hashMap);

	List<Orgnews> findPageListByDto(HashMap<String, Object> hashMap);

	long findCountByDto(HashMap<String, Object> hashMap);

}
