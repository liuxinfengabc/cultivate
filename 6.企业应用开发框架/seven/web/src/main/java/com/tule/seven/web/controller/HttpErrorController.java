package com.tule.seven.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HttpErrorController implements ErrorController {
//    private static final String ERROR_PATH = "/error";
//
//    @RequestMapping(value = ERROR_PATH)
//    public String handleERror(){
//        return "errorpage/404";
//    }
//
//    @Override
//    public String getErrorPath(){
//        return ERROR_PATH;
//    }
    @Override
    public String getErrorPath(){
        return "/error";
    }
    @RequestMapping(value = {"/error"})
    @ResponseBody
    public Object error(HttpServletRequest request){
        Map<String , Object> body = new HashMap<>();
        body.put("error","not found");
        body.put("错误类型","404");
        return body;
    }
}
