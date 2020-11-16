package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.model.Message;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.MessageService;
import com.lz.ht.util.JwtUtil;
import com.lz.ht.base.BaseController;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.github.abel533.sql.SqlMapper;
@Slf4j
@Controller
public class MessageController extends BaseController{

    @Autowired
    private MessageService messageServiceImpl;


    @RequestMapping(value = "/message/list",method = {RequestMethod.GET})
    public String message_list()throws Exception{
        return "message/message_list";
    }



    @RequestMapping(value = "/message/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(Message message, PageModel<Message> page)throws Exception{
           page.init();
           List<Message> list = messageServiceImpl.findPageList(page,message);
           long count = messageServiceImpl.findCount(message);
           page.packData(count,list);
           return page;
    }

    @RequestMapping(value = "/message/add",method = {RequestMethod.GET})
    public String addInit(Message message,Model model){
        return "message/message_add";
    }

    @RequestMapping(value = "/message/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(Message message){
        messageServiceImpl.add(message);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/message/update",method = {RequestMethod.GET})
    public String updateInit(Message message,Model model){
        message = messageServiceImpl.findById(message.getId());
        model.addAttribute("message",message);
        return "message/message_update";
    }

    @RequestMapping(value = "/message/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(Message message){
        messageServiceImpl.updateById(message);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/message/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(Message message){
        messageServiceImpl.deleteById(message.getId());
        return Result.genSuccessResult();
    }



    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/message/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "message/message_selectPageList";
    }

    @RequestMapping(value = "/message/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(Message message, PageModel<Message> page)throws Exception{
        page.init();
        List<Message> list = messageServiceImpl.findPageList(page,message);
        long count = messageServiceImpl.findCount(message);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/message/choosePage",method = {RequestMethod.GET})
    public String selectPage(String fkName,Model model){
        if(StringUtils.isNotEmpty(fkName)){
            Map<String, Object> foreignKeyMap = sqlMapper.selectOne("select * from t_fkeys where fkName = \'"+ fkName+"\'" );
            String rSql = foreignKeyMap.get("rSql").toString();
            log.info("rSql:{}",rSql);
            List<Map<String, Object>> relativeMapList = sqlMapper.selectList(rSql);
            if(relativeMapList!=null){
                model.addAttribute("relativeMapList",relativeMapList);
                String rType = foreignKeyMap.get("rType").toString();
                if("0".equals(rType)) return select_radioPage(model);
                if("1".equals(rType)) return select_radioPage(model);
                if("2".equals(rType)) {
                    String coverOtherValueColumn = foreignKeyMap.get("coverOtherValueColumn").toString();
                    model.addAttribute("coverOtherValueColumn",coverOtherValueColumn);
                    model.addAttribute("treeNodes",toJson(relativeMapList));
                    return select_zTreePage(model);
                }
            }else{
                return "error/error";
            }
        }
        return "error/error";
    }






    private String select_radioPage(Model model){
        model.addAttribute("demo","demoValue");
        return "message/message_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "message/message_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
