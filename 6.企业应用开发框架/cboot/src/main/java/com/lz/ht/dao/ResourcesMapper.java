package com.lz.ht.dao;

import com.lz.ht.model.Resources;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface ResourcesMapper {

    void add(Resources record);

    void updateById(Resources record);

    int deleteById(Long ResourcesId);

    Resources  findById(Long id);

    List<Resources> findAll();

    List<Resources> findList(Resources record);

    long findCount(Resources resources);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<Resources> findListByMapLimit(HashMap<String,Object> hashMap);

}
