package com.lz.ht.base.exadvice;


import com.lz.ht.base.result.Result;
import com.lz.ht.base.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@ControllerAdvice(basePackages = {"com.lz.ht.controller"})
public class GlobalExceptionHandler  {
    //有多个@ExceptionHandler注解的方法时，会根据抛出异常类去寻找处理方法，如果没有，就往上找父类，直到找到为止。
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request,Exception e){
        log(e, request);
        if(e instanceof SysException){
            SysException myException = (SysException) e;
            return  ResultUtil.error(myException.getCode(),myException.getMessage());
        }
        return ResultUtil.error("1000","业务繁忙");
    }

    private void log(Exception ex, HttpServletRequest request) {
        logger.error("异常开始：异常信息：", ex);
        logger.error("请求地址：" + request.getRequestURL());
        Enumeration enumeration = request.getParameterNames();
        logger.error("请求参数：");
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            logger.error(name + "---" + request.getParameter(name));
        }

        StackTraceElement[] error = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : error) {
            logger.error(stackTraceElement.toString());
        }
        logger.error("异常结束");
    }

}
