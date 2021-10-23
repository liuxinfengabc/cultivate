package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.model.User;
import com.lz.ht.model.UserRole;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.UserRoleService;
import com.lz.ht.service.UserService;
import com.lz.ht.util.JwtUtil;
import com.lz.ht.util.MD5Util;
import com.lz.ht.base.BaseController;
import com.lz.ht.constant.SysConstant;

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
public class UserController extends BaseController{

    @Autowired
    private UserService userServiceImpl; 
    
    @Autowired
    private UserRoleService userRoleServiceImpl;
    
    @RequestMapping(value = "/user/list",method = {RequestMethod.GET})
    public String user_list()throws Exception{
        return "user/user_list";
    }



    @RequestMapping(value = "/user/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(User user, PageModel<User> page)throws Exception{
        page.init();
        List<User> list = userServiceImpl.findPageList(page,user);
        long count = userServiceImpl.findCount(user);
        page.packData(count,list);
        return page;
    }

    @RequestMapping(value = "/user/add",method = {RequestMethod.GET})
    public String addInit(User user,Model model){
        return "user/user_add";
    }

    @RequestMapping(value = "/user/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(User user){
    	String password = MD5Util.getMD5(SysConstant.INIT_PASSWORD);
    	user.setPassword(password);
        userServiceImpl.add(user);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/user/update",method = {RequestMethod.GET})
    public String updateInit(User user,Model model){
        user = userServiceImpl.findById(user.getId());
        model.addAttribute("user",user);
        return "user/user_update";
    }

    @RequestMapping(value = "/user/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(User user){
        userServiceImpl.updateById(user);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/user/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(User user){
    	 //查询，此用户的角色，将此表t_user_role用户的角色一并删除
    	userServiceImpl.deleteUserCascade(user.getId()); 
        return Result.genSuccessResult();
    }



    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/user/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "user/user_selectPageList";
    }

    @RequestMapping(value = "/user/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(User user, PageModel<User> page)throws Exception{
        page.init();
        List<User> list = userServiceImpl.findPageList(page,user);
        long count = userServiceImpl.findCount(user);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/user/choosePage",method = {RequestMethod.GET})
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


    @RequestMapping(value = "/user/giveRole",method = {RequestMethod.GET})
    public String giveRoleInit(User user,Model model){
    	Long userId = user.getId();
    	//根据UserId 获取本用户的角色
    	
    	
    	//1.获取所有的角色列表
    	//2.获取本用户的角色
    	//3.如果所有角色列表中有本用户的角色，就选中
    	String sql = "   SELECT  rol.roleKey AS id, " + 
    			"    rol.roleDesc AS name, " + 
    			"    rol.pRoleKey AS pId, " + 
    			"    'true' AS open,  " + 
    			"    CASE WHEN   rol.roleKey IN ( SELECT r.roleKey FROM t_user_role r WHERE r.userId = "
    			+ " #{userId} )  THEN 'true' ELSE 'false' END AS checked  " + 
    			" FROM  t_role rol "; 
    	List<Map> selectList = sqlMapper.selectList(sql, userId, Map.class);
       model.addAttribute("treeNodes",toJson(selectList)); 
   	   model.addAttribute("userId",userId);  
        return "user/give_role";
    }

    
    @RequestMapping(value = "/user/giveRole",method = {RequestMethod.POST})
    @ResponseBody
    public Result giveRole(String roleKeys,String userId){
    	 log.info(roleKeys); 
    	 log.info(userId); 
    	 if("".equals(roleKeys)||"".equals(userId)) return Result.genSuccessResult();
    	 //保存此roleKey对应的resKeys 到表t_role_resources
    	 String[] roleKeysArr = roleKeys.split("##");
    	 Long uid = Long.valueOf(userId);
    	 userRoleServiceImpl.updateRoleKeys(roleKeysArr,uid);
    	
    	 return Result.genSuccessResult();
    }


    private String select_radioPage(Model model){
        model.addAttribute("demo","demoValue");
        return "user/user_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "user/user_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
