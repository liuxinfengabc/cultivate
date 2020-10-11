package com.tule.dao;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tb_user
 * @author 
 */
@Data
public class TbUser implements Serializable {
    /**
     * 用户名
     */
    private String userid;

    private String usertype;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 密码
     */
    private String userpwd;

    private String operator;

    private Date operatedtime;

    private static final long serialVersionUID = 1L;
}