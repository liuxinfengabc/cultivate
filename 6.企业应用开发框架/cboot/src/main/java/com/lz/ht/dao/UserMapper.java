package com.lz.ht.dao;

import com.lz.ht.model.User;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface UserMapper {

    void add(User record);

    void updateById(User record);

    int deleteById(Long UserId);

    User  findById(Long id);

    List<User> findAll();

    List<User> findList(User record);

    long findCount(User user);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<User> findListByMapLimit(HashMap<String,Object> hashMap);

}
