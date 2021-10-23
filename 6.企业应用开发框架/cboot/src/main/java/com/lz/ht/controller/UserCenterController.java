package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.result.ResultData;
import com.lz.ht.model.Message;
import com.lz.ht.model.User;
import com.lz.ht.model.UserExt;
import com.lz.ht.page.PageModel;
import com.lz.ht.service.MessageService;
import com.lz.ht.service.UserExtService;
import com.lz.ht.service.UserRoleService;
import com.lz.ht.service.UserService;
import com.lz.ht.util.ToolKit;
import com.lz.ht.base.BaseController;
import com.lz.ht.constant.SysConstant;
import com.lz.ht.dto.MessageDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Slf4j
@Controller
public class UserCenterController extends BaseController{

	    @Autowired
	    private UserService userServiceImpl; 
	    
	    @Autowired
	    private UserRoleService userRoleServiceImpl;
	    
	    @Autowired
	    private UserExtService userExtServiceImpl;
	    
	    
	    @RequestMapping(value = "/userCenter/userInfo",method = {RequestMethod.GET})
	    public String userInfo(Model model){
	    	Subject subject = SecurityUtils.getSubject();
	    	Long loginUserId  = (Long)subject.getSession().getAttribute("loginUserId"); 
	    	User user2 = userServiceImpl.findById(loginUserId);
	    	UserExt userExt = new UserExt();
	    	userExt.setUserId(loginUserId);
	    	userExt = userExtServiceImpl.findOne(userExt);
	        model.addAttribute("user",user2);
	        model.addAttribute("userExt",userExt);
	        return "userCenter/user_userInfo";
	    }

	 
    
     
	    @RequestMapping(value = "/userCenter/updatePwd",method = {RequestMethod.POST})
	    @ResponseBody
	    public Result updatePwd(User user){
	        userServiceImpl.updateById(user);
	        return Result.genSuccessResult();
	    }
	    
	
	    @RequestMapping(value = "/userCenter/updateUserExt",method = {RequestMethod.POST})
	    @ResponseBody
	    public Result updateUserExt(UserExt userExt){
	    	
	    	 UserExt record = new UserExt();
	    	 record.setUserId(userExt.getUserId());
             List<UserExt> findList = userExtServiceImpl.findList(record);
             if(findList!=null&&findList.size()>0) {
          	   //更新
          	   UserExt userExtDB = findList.get(0);
          	   userExt.setId(userExtDB.getId());
          	   Timestamp updateTime = new Timestamp(new Date().getTime());
          	   record.setUpdateTime(updateTime);
          	   userExtServiceImpl.updateById(userExt);
          	   
             }else {
          	   //新增
          	   
          	   Timestamp updateTime = new Timestamp(new Date().getTime());
          	   record.setUpdateTime(updateTime);
          	   userExtServiceImpl.add(userExt);
             }
	    	
	        return Result.genSuccessResult();
	    }
	    
	    
	    
	     
	    
	    @RequestMapping(value = "/userCenter/updatePic",method = {RequestMethod.POST})
	    @ResponseBody
	    public Result updatePic(User user ,@RequestParam("myPic") MultipartFile myPic){
	    	
	    	if(myPic!=null){
                String uuid = UUID.randomUUID().toString();
                String originalFileName = myPic.getOriginalFilename();
                int index = originalFileName.lastIndexOf(".");
                String imageType =  ".jpeg";
                String suffixName = originalFileName.substring(index);
                if(suffixName.length()>0) { 
                	if(SysConstant.IMAGE_ALLOW_EXT.contains(suffixName))
                	{
                		imageType = suffixName;
                	} else {
                		return Result.genResult("899","图片格式不符合规则, " + SysConstant.IMAGE_ALLOW_EXT);
                	}
                }   
                String baseImagePath = SysConstant.getIMAGE_ROOT_PATH();
                String fileRealPath = baseImagePath + "/" + uuid +  imageType ;
                File toFile  = new File(fileRealPath);
                //另存临时文件到指定路径
                log.info("imageType " + imageType);
                try {
                    myPic.transferTo(toFile);
                } catch (IOException e) {
                    log.info("",e);
                }
               String image = "/localImage/image/" + uuid + imageType ;
               
               UserExt record = new UserExt();
               record.setUserId(user.getId()); 
               List<UserExt> findList = userExtServiceImpl.findList(record);
               if(findList!=null&&findList.size()>0) {
            	   //更新
            	   record.setUserPic(image);
            	   UserExt userExt = findList.get(0);
            	   record.setId(userExt.getId());
            	   Timestamp updateTime = new Timestamp(new Date().getTime());
            	   record.setUpdateTime(updateTime);
            	   userExtServiceImpl.updateById(record);
            	   
               }else {
            	   //新增
            	   record.setUserPic(image);
            	   Timestamp updateTime = new Timestamp(new Date().getTime());
            	   record.setUpdateTime(updateTime);
            	   userExtServiceImpl.add(record);
               }
               return Result.genSuccessResult();
              
            }
	    	return Result.genResult("898","图片不存在！");

	    }
	    
	    
	    
	    
	    
	    @Autowired
	    private MessageService messageServiceImpl;

	    /**
	     * 我的私信
	     * @return
	     * @throws Exception
	     */
	    @RequestMapping(value = "/userCenter/personalMsg",method = {RequestMethod.GET})
	    public String message_list()throws Exception{
	        return "userCenter/personalMsg_list";
	    }

	    
	    /**
	     * 我的私信
	     * @return
	     * @throws Exception
	     */
	    @RequestMapping(value = "/userCenter/personalMsg",method = {RequestMethod.POST})
	    @ResponseBody
	    public PageModel list(MessageDto messageDto, PageModel<MessageDto> page)throws Exception{
	           page.init(); 
	           Long currentUserId = super.getCurrentUserId();
	           User user = userServiceImpl.findById(currentUserId);
	           if(messageDto.getMsgType()==null) {
	        	   messageDto.setMsgType(0);
	           }
	           if(messageDto.getMsgType()==0) {// 0 我发送给别人 1 别人发送给我的
	        	   messageDto.setFromUserId(currentUserId);
	        	   UserExt  record = new UserExt();
	        	   record.setUserId(currentUserId);
	        	   UserExt userExt = userExtServiceImpl.findOne(record);
	        	   messageDto.setFromUserPic(userExt.getUserPic());
	        	   messageDto.setFromUserName(user.getUserName());
	        	   messageDto.setFromUserDeptName(user.getDeptName());     	   
	           }
	           if(messageDto.getMsgType()==1) {
	        	   messageDto.setToUserId(currentUserId);
	        	   messageDto.setToUserName(user.getUserName());
	        	   messageDto.setToUserDeptName(user.getDeptName());  
	           }
	           List<MessageDto> list = messageServiceImpl.findPageListDto(page, messageDto);
	           for (MessageDto dto : list) {
	        	   String conversationId = dto.getConversationId();
	        	   long subListNumber = messageServiceImpl.findSubListNumberByCid(conversationId);
	        	   dto.setSubListNumber(subListNumber);
			   }
	           
	           long count = messageServiceImpl.findCountDto(messageDto);           
	           page.packData(count,list);
	           return page;
	    }
	    
	    
	    /**
	     * 我的私信  子列表
	     * @return
	     * @throws Exception
	     */
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/userCenter/personalMsgSublist",method = {RequestMethod.POST})
	    @ResponseBody
	    public ResultData<List<MessageDto>> personalMsgSublist(MessageDto messageDto, PageModel<MessageDto> page)throws Exception{
	    	     //根据conversationId 查询子列表
	           List<MessageDto> list = messageServiceImpl.findSubListByDto(messageDto); 
	            
	           return ResultData.genSuccessResultData(list);
	    }
	    
	    
	    @RequestMapping(value = "/userCenter/sendMsg",method = {RequestMethod.POST})
	    @ResponseBody
	    public Result sendMsg(MessageDto messageDto ){
	    	
	    	//查找此会话要发送的对象
	    	String conversationId = messageDto.getConversationId();
	    	MessageDto conversation = messageServiceImpl.findConversationByCid(conversationId);
	    	Message msg = new Message();
	    	Long nextCorder = conversation.getCurrMaxCorder() + 1L;
	    	Long uid1 = conversation.getFromUserId();
	    	Long uid2 = conversation.getToUserId(); 
	    	Long currentUserId = getCurrentUserId();
	    	messageDto.setFromUserId(currentUserId);
	    	if(currentUserId.longValue() == uid1.longValue() ) {
	    		msg.setFromUserId(currentUserId);
	    		msg.setToUserId(uid2);
	    	}
            if(currentUserId.longValue() == uid2.longValue() ) {
            	msg.setFromUserId(currentUserId);
            	msg.setToUserId(uid1);
	    	}
            msg.setCorder(nextCorder.intValue());
            msg.setAddTime(new Date());
            msg.setStatus(0L);
            msg.setMsgContent(messageDto.getMsgContent());
            //corder>5时将重新开一个会话
            if(nextCorder>5) {
            	msg.setConversationId(ToolKit.uuidString());
            	msg.setCorder(0);
            }else { 
            	 msg.setConversationId(conversationId);	
            } 
            messageServiceImpl.add(msg);
	    	return Result.genSuccessResult();
	    }
	    	
	     
	    @RequestMapping(value = "/userCenter/publicMsg",method = {RequestMethod.GET})
	    public String publicMsg() {
	    	
	    	
			return "userCenter/news"; 
	    }
	    
}
