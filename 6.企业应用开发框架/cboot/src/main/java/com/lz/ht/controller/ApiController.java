package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 */
@Slf4j
@Controller
@RequestMapping("/api")
public class ApiController {

    /**
     * api 登录
     * @param userName
     * @param Password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public Result login(HttpServletResponse response, @RequestParam("username") String userName, @RequestParam("password") String Password) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, Password);
        //登录
        currentUser.login(token);
        response.setHeader("xToken", JwtUtil.createJWT(userName,"subject",60*1000));
        return Result.genSuccessResult();
    }



}
