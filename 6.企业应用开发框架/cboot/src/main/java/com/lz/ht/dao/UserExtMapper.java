package com.lz.ht.dao;

import com.lz.ht.model.UserExt;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface UserExtMapper {

    void add(UserExt record);

    void updateById(UserExt record);

    int deleteById(Long UserExtId);

    UserExt  findById(Long id);

    List<UserExt> findAll();

    List<UserExt> findList(UserExt record);

    long findCount(UserExt userExt);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<UserExt> findListByMapLimit(HashMap<String,Object> hashMap);

}
