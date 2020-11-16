package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.model.SysConfig;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.SysConfigService;
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
public class SysConfigController extends BaseController{

    @Autowired
    private SysConfigService sysConfigServiceImpl;


    @RequestMapping(value = "/sysConfig/list",method = {RequestMethod.GET})
    public String sysConfig_list()throws Exception{
        return "sysConfig/sysConfig_list";
    }



    @RequestMapping(value = "/sysConfig/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(SysConfig sysConfig, PageModel<SysConfig> page)throws Exception{
           page.init();
           List<SysConfig> list = sysConfigServiceImpl.findPageList(page,sysConfig);
           long count = sysConfigServiceImpl.findCount(sysConfig);
           page.packData(count,list);
           return page;
    }

    @RequestMapping(value = "/sysConfig/add",method = {RequestMethod.GET})
    public String addInit(SysConfig sysConfig,Model model){
        return "sysConfig/sysConfig_add";
    }

    @RequestMapping(value = "/sysConfig/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(SysConfig sysConfig){
        sysConfigServiceImpl.add(sysConfig);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/sysConfig/update",method = {RequestMethod.GET})
    public String updateInit(SysConfig sysConfig,Model model){
        sysConfig = sysConfigServiceImpl.findById(sysConfig.getId());
        model.addAttribute("sysConfig",sysConfig);
        return "sysConfig/sysConfig_update";
    }

    @RequestMapping(value = "/sysConfig/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(SysConfig sysConfig){
        sysConfigServiceImpl.updateById(sysConfig);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/sysConfig/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(SysConfig sysConfig){
        sysConfigServiceImpl.deleteById(sysConfig.getId());
        return Result.genSuccessResult();
    }


    @RequestMapping(value = "/sysConfig/icons", method = RequestMethod.GET)
	public String icons(){
		
		return "sysConfig/icons_unicode";
	}

    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/sysConfig/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "sysConfig/sysConfig_selectPageList";
    }

    @RequestMapping(value = "/sysConfig/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(SysConfig sysConfig, PageModel<SysConfig> page)throws Exception{
        page.init();
        List<SysConfig> list = sysConfigServiceImpl.findPageList(page,sysConfig);
        long count = sysConfigServiceImpl.findCount(sysConfig);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/sysConfig/choosePage",method = {RequestMethod.GET})
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
        return "sysConfig/sysConfig_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "sysConfig/sysConfig_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
