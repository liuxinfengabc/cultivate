package com.lz.ht.base;

import com.lz.ht.base.result.Result;
import com.lz.ht.base.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 */
@Slf4j
@Controller
@RequestMapping("/error")
public class ExceptionController  implements ErrorController {

    @RequestMapping("/noToken")
    @ResponseBody
    public Result noToken(){
        return ResultUtil.error("9999","NO_TOKEN");
    }

    @Override
    public String getErrorPath() {
        return "error/error";
    }
}
