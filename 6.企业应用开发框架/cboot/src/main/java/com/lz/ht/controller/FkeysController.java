package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.model.Fkeys;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.FkeysService;
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
public class FkeysController extends BaseController{

    @Autowired
    private FkeysService fkeysServiceImpl;


    @RequestMapping(value = "/fkeys/list",method = {RequestMethod.GET})
    public String fkeys_list()throws Exception{
        return "fkeys/fkeys_list";
    }



    @RequestMapping(value = "/fkeys/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(Fkeys fkeys, PageModel<Fkeys> page)throws Exception{
           page.init();
           List<Fkeys> list = fkeysServiceImpl.findPageList(page,fkeys);
           long count = fkeysServiceImpl.findCount(fkeys);
           page.packData(count,list);
           return page;
    }

    @RequestMapping(value = "/fkeys/add",method = {RequestMethod.GET})
    public String addInit(Fkeys fkeys,Model model){
        return "fkeys/fkeys_add";
    }

    @RequestMapping(value = "/fkeys/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(Fkeys fkeys){
        fkeysServiceImpl.add(fkeys);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/fkeys/update",method = {RequestMethod.GET})
    public String updateInit(Fkeys fkeys,Model model){
        fkeys = fkeysServiceImpl.findById(fkeys.getId());
        model.addAttribute("fkeys",fkeys);
        return "fkeys/fkeys_update";
    }

    @RequestMapping(value = "/fkeys/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(Fkeys fkeys){
        fkeysServiceImpl.updateById(fkeys);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/fkeys/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(Fkeys fkeys){
        fkeysServiceImpl.deleteById(fkeys.getId());
        return Result.genSuccessResult();
    }



    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/fkeys/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "fkeys/fkeys_selectPageList";
    }

    @RequestMapping(value = "/fkeys/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(Fkeys fkeys, PageModel<Fkeys> page)throws Exception{
        page.init();
        List<Fkeys> list = fkeysServiceImpl.findPageList(page,fkeys);
        long count = fkeysServiceImpl.findCount(fkeys);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/fkeys/choosePage",method = {RequestMethod.GET})
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
        return "fkeys/fkeys_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "fkeys/fkeys_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
