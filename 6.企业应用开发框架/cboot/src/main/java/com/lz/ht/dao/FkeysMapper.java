package com.lz.ht.dao;

import com.lz.ht.model.Fkeys;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface FkeysMapper {

    void add(Fkeys record);

    void updateById(Fkeys record);

    int deleteById(Long FkeysId);

    Fkeys  findById(Long id);

    List<Fkeys> findAll();

    List<Fkeys> findList(Fkeys record);

    long findCount(Fkeys fkeys);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<Fkeys> findListByMapLimit(HashMap<String,Object> hashMap);

}
