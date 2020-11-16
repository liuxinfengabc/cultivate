package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.model.Orgcomment;
import com.lz.ht.model.Orgnews;
import com.lz.ht.model.User;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.OrgcommentService;
import com.lz.ht.service.OrgnewsService;
import com.lz.ht.service.UserService;
import com.lz.ht.util.JwtUtil;
import com.lz.ht.base.BaseController;
import com.lz.ht.dto.OrgcommentDto;

import java.util.Date;
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
import org.springframework.web.util.HtmlUtils;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.github.abel533.sql.SqlMapper;
@Slf4j
@Controller
public class OrgcommentController extends BaseController{

    @Autowired
    private OrgcommentService orgcommentServiceImpl;
    @Autowired
    private UserService userService;
    @Autowired
    private OrgnewsService orgnewsService;

    @RequestMapping(value = "/orgcomment/list",method = {RequestMethod.GET})
    public String orgcomment_list()throws Exception{
        return "orgcomment/comment_list";
    }



    @RequestMapping(value = "/orgcomment/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(OrgcommentDto orgcommentDto, PageModel<OrgcommentDto> page)throws Exception{
           page.init(); 
           List<OrgcommentDto> list = orgcommentServiceImpl.findPageListByDto(page,orgcommentDto);
           for (OrgcommentDto org : list) {
        	   org.setNcomment(HtmlUtils.htmlUnescape(org.getNcomment()));
		   }
           long count = orgcommentServiceImpl.findCountByDto(orgcommentDto);
           page.packData(count,list);
           return page;
    }

    @RequestMapping(value = "/orgcomment/add",method = {RequestMethod.GET})
    public String addInit(Orgcomment orgcomment,Model model){
        return "orgcomment/comment_add";
    }

    @RequestMapping(value = "/orgcomment/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(Orgcomment orgcomment){  
    	Orgnews orgnews = orgnewsService.findById(orgcomment.getNewsId()); 
    	if(orgnews.getAllowComment().intValue()==1) {
    		return Result.genResult("699", "评论权限已经关闭，无法评论");
    	}  
    	Long currentUserId = getCurrentUserId();
    	User user = userService.findById(currentUserId);
    	orgcomment.setUserId(currentUserId);
    	orgcomment.setAddTime(new Date());
    	orgcomment.setUserName(user.getUserName());
    	orgcomment.setNcomment(HtmlUtils.htmlEscape(orgcomment.getNcomment()));
    	orgcomment.setCstate(0L);
        orgcommentServiceImpl.add(orgcomment);
        return Result.genSuccessResult();
    }

//    @RequestMapping(value = "/orgcomment/update",method = {RequestMethod.GET})
//    public String updateInit(Orgcomment orgcomment,Model model){
//        orgcomment = orgcommentServiceImpl.findById(orgcomment.getId());
//        model.addAttribute("orgcomment",orgcomment);
//        return "orgcomment/orgcomment_update";
//    }

//    @RequestMapping(value = "/orgcomment/update",method = {RequestMethod.POST})
//    @ResponseBody
//    public Result update(Orgcomment orgcomment){
//        orgcommentServiceImpl.updateById(orgcomment);
//        return Result.genSuccessResult();
//    }

    @RequestMapping(value = "/orgcomment/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(Orgcomment orgcomment){
        orgcommentServiceImpl.deleteById(orgcomment.getId());
        return Result.genSuccessResult();
    }



    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/orgcomment/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "orgcomment/orgcomment_selectPageList";
    }

    @RequestMapping(value = "/orgcomment/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(Orgcomment orgcomment, PageModel<Orgcomment> page)throws Exception{
        page.init();
        List<Orgcomment> list = orgcommentServiceImpl.findPageList(page,orgcomment);
        long count = orgcommentServiceImpl.findCount(orgcomment);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/orgcomment/choosePage",method = {RequestMethod.GET})
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
        return "orgcomment/orgcomment_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "orgcomment/orgcomment_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
