package com.lz.ht.service;

import com.lz.ht.model.User;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface UserService {

    void add(User record);

    void updateById(User record);

    int deleteById(Long userId);

    User findById(Long userId);

    List<User> findAll();

    User findOne(User record);

    List<User> findList(User record);

    List<User> findPageList(PageModel<User> page, User user) throws Exception;

    List<User> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(User user) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

	void deleteUserCascade(Long id);
 
}
