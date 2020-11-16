package com.lz.ht.service;

import com.lz.ht.model.UserExt;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface UserExtService {

    void add(UserExt record);

    void updateById(UserExt record);

    int deleteById(Long userExtId);

    UserExt findById(Long userExtId);

    List<UserExt> findAll();

    UserExt findOne(UserExt record);

    List<UserExt> findList(UserExt record);

    List<UserExt> findPageList(PageModel<UserExt> page, UserExt userExt) throws Exception;

    List<UserExt> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(UserExt userExt) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

}
