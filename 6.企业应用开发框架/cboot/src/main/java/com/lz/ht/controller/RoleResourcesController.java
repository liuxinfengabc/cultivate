package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.model.RoleResources;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.RoleResourcesService;
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
public class RoleResourcesController extends BaseController{

    @Autowired
    private RoleResourcesService roleResourcesServiceImpl;


    @RequestMapping(value = "/roleResources/list",method = {RequestMethod.GET})
    public String roleResources_list()throws Exception{
        return "roleResources/roleResources_list";
    }



    @RequestMapping(value = "/roleResources/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(RoleResources roleResources, PageModel<RoleResources> page)throws Exception{
           page.init();
           List<RoleResources> list = roleResourcesServiceImpl.findPageList(page,roleResources);
           long count = roleResourcesServiceImpl.findCount(roleResources);
           page.packData(count,list);
           return page;
    }

    @RequestMapping(value = "/roleResources/add",method = {RequestMethod.GET})
    public String addInit(RoleResources roleResources,Model model){
        return "roleResources/roleResources_add";
    }

    @RequestMapping(value = "/roleResources/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(RoleResources roleResources){
        roleResourcesServiceImpl.add(roleResources);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/roleResources/update",method = {RequestMethod.GET})
    public String updateInit(RoleResources roleResources,Model model){
        roleResources = roleResourcesServiceImpl.findById(roleResources.getId());
        model.addAttribute("roleResources",roleResources);
        return "roleResources/roleResources_update";
    }

    @RequestMapping(value = "/roleResources/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(RoleResources roleResources){
        roleResourcesServiceImpl.updateById(roleResources);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/roleResources/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(RoleResources roleResources){
        roleResourcesServiceImpl.deleteById(roleResources.getId());
        return Result.genSuccessResult();
    }



    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/roleResources/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "roleResources/roleResources_selectPageList";
    }

    @RequestMapping(value = "/roleResources/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(RoleResources roleResources, PageModel<RoleResources> page)throws Exception{
        page.init();
        List<RoleResources> list = roleResourcesServiceImpl.findPageList(page,roleResources);
        long count = roleResourcesServiceImpl.findCount(roleResources);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/roleResources/choosePage",method = {RequestMethod.GET})
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
        return "roleResources/roleResources_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "roleResources/roleResources_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
