package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.model.Role;
import com.lz.ht.model.RoleResources;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.RoleResourcesService;
import com.lz.ht.service.RoleService;
import com.lz.ht.util.JwtUtil;
import com.lz.ht.base.BaseController;

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
public class RoleController extends BaseController{

    @Autowired
    private RoleService roleServiceImpl;

    @Autowired
    private RoleResourcesService roleResourcesService;

    @RequestMapping(value = "/role/list",method = {RequestMethod.GET})
    public String role_list()throws Exception{
        return "role/role_list";
    }



    @RequestMapping(value = "/role/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(Role role, PageModel<Role> page)throws Exception{
           page.init();
           List<Role> list = roleServiceImpl.findPageList(page,role);
           long count = roleServiceImpl.findCount(role);
           page.packData(count,list);
           return page;
    }

    @RequestMapping(value = "/role/add",method = {RequestMethod.GET})
    public String addInit(Role role,Model model){
        return "role/role_add";
    }

    @RequestMapping(value = "/role/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(Role role){
        roleServiceImpl.add(role);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/role/update",method = {RequestMethod.GET})
    public String updateInit(Role role,Model model){
        role = roleServiceImpl.findById(role.getId());
        model.addAttribute("role",role);
        return "role/role_update";
    }

    @RequestMapping(value = "/role/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(Role role){
        roleServiceImpl.updateById(role);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/role/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(Role role){
        roleServiceImpl.deleteById(role.getId());
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/role/roleResource",method = {RequestMethod.GET})
    public String roleResourceInit(Role role,Model model) {
    	  role = roleServiceImpl.findById(role.getId());
    	  //根据role 查找roleResource表中，本角色管理的资源
    	  //SELECT  t.id AS id, t.deptName AS `name`, t.parentId pId , 'true' AS `open` FROM t_dept t
    	  String roleKey = role.getRoleKey();
    	  //1.查询所有的资源列表
    	  //2.查询角色对应的资源
    	  //3.遍历所有资源列表，如果角色对应的资源列表中有，就给他标识为选中状态
    	  String sql = "    SELECT  res.resKey AS id, " + 
    	  		"    res.name AS name, " + 
    	  		"    res.presKey AS pId, " + 
    	  		"    'true' AS open,     " + 
    	  		"    CASE WHEN   res.resKey IN ( SELECT r.resKey FROM t_role_resources r WHERE r.roleKey = "+ 
    	  		"  #{roleKey} )  THEN 'true' ELSE 'false' END AS checked  " + 
    	  		" FROM  t_resources res ";
    	  
    	  List<Map> selectList = sqlMapper.selectList(sql,  roleKey, Map.class); 
    	 
    	  model.addAttribute("treeNodes",toJson(selectList)); 
    	  model.addAttribute("roleKey",roleKey); 
          return "role/role_resource";
    }
    

    @RequestMapping(value = "/role/roleResource",method = {RequestMethod.POST})
    @ResponseBody
    public Result roleResource(String resKeys,String roleKey,Model model) {
    	 log.info(resKeys); 
    	 log.info(roleKey); 
    	 if("".equals(resKeys)||"".equals(roleKey)) return Result.genSuccessResult();
    	 //保存此roleKey对应的resKeys 到表t_role_resources
    	 String[] resKeysArr = resKeys.split("##");
    	 
    	 roleResourcesService.updateResKeys(resKeysArr,roleKey);
    	 
    	 
    	 
    	 return Result.genSuccessResult();
    }
    
    

    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/role/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "role/role_selectPageList";
    }

    @RequestMapping(value = "/role/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(Role role, PageModel<Role> page)throws Exception{
        page.init();
        List<Role> list = roleServiceImpl.findPageList(page,role);
        long count = roleServiceImpl.findCount(role);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/role/choosePage",method = {RequestMethod.GET})
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
        return "role/role_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "role/role_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
