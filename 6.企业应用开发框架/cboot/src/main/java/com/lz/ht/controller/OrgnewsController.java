package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.result.SimditorResult;
import com.lz.ht.model.Orgnews;
import com.lz.ht.model.User;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.OrgnewsService;
import com.lz.ht.service.UserService;
import com.lz.ht.util.JwtUtil;
import com.lz.ht.util.StaticHtmlUtil;

import freemarker.template.TemplateException;

import com.lz.ht.base.BaseController;
import com.lz.ht.constant.SysConstant;
import com.lz.ht.dto.OrgnewsDto;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.github.abel533.sql.SqlMapper;
@Slf4j
@Controller
public class OrgnewsController extends BaseController{

    @Autowired
    private OrgnewsService orgnewsServiceImpl;
    @Autowired
    private UserService userServiceImpl; 

    @RequestMapping(value = "/orgnews/list",method = {RequestMethod.GET})
    public String orgnews_list()throws Exception{
        return "orgnews/orgnews_list";
    }



    @RequestMapping(value = "/orgnews/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(OrgnewsDto orgnewsDto, PageModel<Orgnews> page)throws Exception{
           page.init();
           List<Orgnews> list = orgnewsServiceImpl.findPageListByDto(page, orgnewsDto);
           long count = orgnewsServiceImpl.findCountByDto(orgnewsDto);
           page.packData(count,list); 
           return page;
    }
   

    @RequestMapping(value = "/orgnews/add",method = {RequestMethod.GET})
    public String addInit(Orgnews orgnews,Model model){
        return "orgnews/orgnews_add";
    }

    @RequestMapping(value = "/orgnews/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(Orgnews orgnews){
    	orgnews.setNcontent(HtmlUtils.htmlEscape(orgnews.getNcontent()));
    	orgnews.setNstate(0L);
    	Long userId = super.getCurrentUserId();
    	orgnews.setUserId(userId);
    	User user = userServiceImpl.findById(userId);
    	orgnews.setUserName(user.getUserName());
        orgnewsServiceImpl.add(orgnews);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/orgnews/update",method = {RequestMethod.GET})
    public String updateInit(Orgnews orgnews,Model model){
        orgnews = orgnewsServiceImpl.findById(orgnews.getId());
        orgnews.setNcontent(HtmlUtils.htmlUnescape(orgnews.getNcontent()));
        model.addAttribute("orgnews",orgnews);
        return "orgnews/orgnews_update";
    }

    @RequestMapping(value = "/orgnews/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(Orgnews orgnews){
        orgnews.setNcontent(HtmlUtils.htmlEscape(orgnews.getNcontent()));
        orgnewsServiceImpl.updateById(orgnews);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/orgnews/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(Orgnews orgnews){
        orgnewsServiceImpl.deleteById(orgnews.getId());
        return Result.genSuccessResult();
    }
    //publish
    @RequestMapping(value = "/orgnews/publish",method = {RequestMethod.POST})
    @ResponseBody
    public Result publish(Orgnews orgnews){
    	Orgnews n1 = new Orgnews();
    	n1.setId(orgnews.getId());
    	n1.setNstate(1L); 
    	n1.setPublishTime(new Date());
    	try {
    		//生成静态文件
        	Map<String,Object> templateVars = new HashMap<String, Object>();
        	Orgnews  orgnewsDb =  orgnewsServiceImpl.findById(orgnews.getId());
        	orgnewsDb.setPublishTime(new Date());
        	orgnewsDb.setNcontent(HtmlUtils.htmlUnescape(orgnewsDb.getNcontent()));
        	
        	templateVars.put("orgnews", orgnewsDb);
        	templateVars.put("orgnewsAuthor", SysConstant.getConfigValueByName("orgnewsAuthor"));
             
        	
        	
			String createPageFileName = StaticHtmlUtil.createHtmlByTemplate("orgnews.ftl", "orgnews", templateVars, SysConstant.getSTATIC_HTML_PATH());
			n1.setNewsPath("/localPage/staticHtml/orgnews/"+createPageFileName);
		} catch (IOException e) {
			log.info(e.getMessage());
		} catch (TemplateException e) {
			log.info(e.getMessage());
		} 
        orgnewsServiceImpl.updateById(n1);
        return Result.genSuccessResult();
    }


    @RequestMapping(value = "/orgnews/publicMsgManage",method = {RequestMethod.GET})
    public String publicMsg() {
    	 
		return "orgnews/orgnews_news"; 
    }
    
    
    @RequestMapping(value = "/orgnews/editOrgnewsInit",method = {RequestMethod.GET})
    public String editOrgnewsInit(String moduleName,Model model) {
	 //     list
	 //    	picLeft
	 //    	picRight
	 //    	picture
	 //    	hotNews
       switch (moduleName) {
        case "picture": 
			return "orgnews/orgnews_uploadPic"; 
		default: 
			return "orgnews/orgnews_selectList"; 
		}
    	 
    }
    
    
    @RequestMapping(value = "/orgnews/uploadPic",method = {RequestMethod.POST})
    @ResponseBody
    public SimditorResult uploadPic(@RequestParam("uploadFile") MultipartFile uploadFile){
    	
    	if(uploadFile!=null){
           String uuid = UUID.randomUUID().toString();
           String originalFileName = uploadFile.getOriginalFilename();
           int index = originalFileName.lastIndexOf(".");
           String imageType =  ".jpeg";
           String suffixName = originalFileName.substring(index);
           if(suffixName.length()>0) { 
        	   imageType = suffixName;
           }   
           String baseImagePath = SysConstant.getIMAGE_ROOT_PATH();
           String fileRealPath = baseImagePath + "/" + uuid +  imageType ;
           File toFile  = new File(fileRealPath);
           log.info("imageType " + imageType);
           try {
        	   uploadFile.transferTo(toFile);
           } catch (IOException e) {
               log.info("",e);
               return   SimditorResult.genFail();
           }
          String image = "/localImage/image/" + uuid + imageType ;
      	  return SimditorResult.genSuccess(image); 
       }
    	return   SimditorResult.genFail(); 
    }
    
    
    
    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/orgnews/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "orgnews/orgnews_selectPageList";
    }

    @RequestMapping(value = "/orgnews/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(Orgnews orgnews, PageModel<Orgnews> page)throws Exception{
        page.init();
        List<Orgnews> list = orgnewsServiceImpl.findPageList(page,orgnews);
        long count = orgnewsServiceImpl.findCount(orgnews);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/orgnews/choosePage",method = {RequestMethod.GET})
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
        return "orgnews/orgnews_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "orgnews/orgnews_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
