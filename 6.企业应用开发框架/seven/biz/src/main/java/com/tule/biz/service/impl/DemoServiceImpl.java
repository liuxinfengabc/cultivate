package com.tule.biz.service.impl;

import com.tule.biz.service.DemoService;
import com.tule.dao.TbUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private TbUserDao userMapper;



    @Override
    public String test(){

        return "test";
        //User user = userDao.selectByPrimaryKey(userid:"admin").toString();
    }

//    @Override
//    public String mybatis(){
//        return userMapper.selectByPrimaryKey( userid: "admin").toString();
//    }

//    public  String  mybatis()  {
//
//        return  userMapper.selectByPrimaryKey("admin").toString();
//    }
    @Override
    public String mybatis(){
        return userMapper.selectByPrimaryKey("admin").toString();
    }
}
