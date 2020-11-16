package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.model.Resources;
import com.lz.ht.model.RoleResources;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.ResourcesService;
import com.lz.ht.util.JwtUtil;
import com.lz.ht.base.BaseController;
import com.lz.ht.constant.SysConstant;

import java.util.ArrayList;
import java.util.HashMap;
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
public class ResourcesController extends BaseController{

    @Autowired
    private ResourcesService resourcesServiceImpl;


    @RequestMapping(value = "/resources/list",method = {RequestMethod.GET})
    public String resources_list()throws Exception{
        return "resources/resources_list";
    }



    @RequestMapping(value = "/resources/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(Resources resources, PageModel<Resources> page)throws Exception{
           page.init();
           List<Resources> list = resourcesServiceImpl.findPageList(page,resources);
           long count = resourcesServiceImpl.findCount(resources);
           page.packData(count,list);
           return page;
    }

    @RequestMapping(value = "/resources/add",method = {RequestMethod.GET})
    public String addInit(Resources resources,Model model){
        return "resources/resources_add";
    }

    @RequestMapping(value = "/resources/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(Resources resources){
        resourcesServiceImpl.add(resources);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/resources/update",method = {RequestMethod.GET})
    public String updateInit(Resources resources,Model model){
        resources = resourcesServiceImpl.findById(resources.getId());
        model.addAttribute("resources",resources);
        return "resources/resources_update";
    }

    @RequestMapping(value = "/resources/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(Resources resources){
        resourcesServiceImpl.updateById(resources);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/resources/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(Resources resources){
    	  
    	// 若此资源有子资源也不能删除
    	String sql = "SELECT * FROM t_resources tmp WHERE tmp.presKey = ( " + 
    			"SELECT r.resKey FROM t_resources r WHERE r.id = #{id} )" ;
    			 
        List<Resources> selectList = sqlMapper.selectList(sql, resources.getId(), Resources.class);
        if(selectList!=null && selectList.size()>0) {
        	return Result.genResult("997", "此资源有子资源，不能删除");
        }
        //查找所有拥有此资源的角色，若有角色使用此资源就提示不能删除
         String sql2 = "SELECT * FROM " +   
         		"  t_role_resources rr " + 
         		"  WHERE rr.resKey IN " + 
         		"  (SELECT " + 
         		"    t2.resKey " + 
         		"  FROM " + 
         		"    t_resources t2 " + 
         		"  WHERE t2.presKey = " + 
         		"    (SELECT " + 
         		"      r.resKey " + 
         		"    FROM " + 
         		"      t_resources r " + 
         		"    WHERE r.id = #{id}) " + 
         		"    UNION " + 
         		"    SELECT " + 
         		"      r.resKey " + 
         		"    FROM " + 
         		"      t_resources r " + 
         		"    WHERE r.id = #{id}) "  ;
 
          
          Map<String,Long> map =new HashMap<String, Long>();
          map.put("id", resources.getId());
          List<RoleResources> selectList2 = sqlMapper.selectList(sql2, map, RoleResources.class);
          if(selectList2!=null && selectList2.size()>0) {
          	return Result.genResult("996", "有角色使用此资源，不能删除");
          }
          //RABC 模型不能删除
           
          // return Result.genResult("995", "系统模块，不能删除");
         
          String sql3 = " SELECT DISTINCT tt.id FROM t_resources tt WHERE tt.resKey IN ( " + 
          		" SELECT  t2.resKey FROM t_resources  t2 WHERE t2.resKey = "+SysConstant.SYSTEM_MANAGE_RESOURCE_RESKEY+" OR t2.presKey = "+SysConstant.SYSTEM_MANAGE_RESOURCE_RESKEY+") OR tt.presKey " + 
          		" IN (SELECT  t1.resKey FROM t_resources  t1 WHERE t1.resKey =  "+SysConstant.SYSTEM_MANAGE_RESOURCE_RESKEY+ " OR t1.presKey =  "+SysConstant.SYSTEM_MANAGE_RESOURCE_RESKEY+ ")";
           
          List<Map> selectList3 = sqlMapper.selectList(sql3, Map.class);
          boolean flag = false;
          for(Map m : selectList3) {
        	  String id = m.get("id").toString();
        	  if(id.equals( resources.getId().toString()) ) {
        		  flag = true;
        		  break;
        	  }  
          }
          if(flag) {
        	  return Result.genResult("995", "系统模块，不能删除");
          }
          resourcesServiceImpl.deleteById(resources.getId());
       
          return Result.genSuccessResult();
    }



    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/resources/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "resources/resources_selectPageList";
    }

    @RequestMapping(value = "/resources/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(Resources resources, PageModel<Resources> page)throws Exception{
        page.init();
        List<Resources> list = resourcesServiceImpl.findPageList(page,resources);
        long count = resourcesServiceImpl.findCount(resources);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/resources/choosePage",method = {RequestMethod.GET})
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
        return "resources/resources_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "resources/resources_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
