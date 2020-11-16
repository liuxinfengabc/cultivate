package com.lz.ht.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lz.ht.constant.SysConstant;
import com.lz.ht.result.SimditorResult;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class SimditorController {

	/***
	 * Simditor 编辑器结果返回规则：
	 *   成功时
		{
		  "success":true,
		  "file_path":"图片的路径地址"
		}  
		 失败时
		{
		  "success":false
		}
	 * @param myPic
	 * @return
	 */
	    @RequestMapping(value = "/simditor/uploadPic",method = {RequestMethod.POST})
	    @ResponseBody
	    public SimditorResult uploadPic(@RequestParam("editorPic") MultipartFile myPic){
	    	
	    	if(myPic!=null){
               String uuid = UUID.randomUUID().toString();
               String originalFileName = myPic.getOriginalFilename();
               int index = originalFileName.lastIndexOf(".");
               String imageType =  ".jpeg";
               String suffixName = originalFileName.substring(index);
               if(suffixName.length()>0) { 
            	   imageType = suffixName;
               }   
               String baseImagePath = SysConstant.getSIMDITOR_IMAGE_PATH();
               String fileRealPath = baseImagePath + "/" + uuid +  imageType ;
               File toFile  = new File(fileRealPath);
               log.info("imageType " + imageType);
               try {
                   myPic.transferTo(toFile);
               } catch (IOException e) {
                   log.info("",e);
                   return   SimditorResult.genFail();
               }
              String image = "/simditor/image/" + uuid + imageType ;
          	  return SimditorResult.genSuccess(image); 
           }
	    	return   SimditorResult.genFail(); 
	    }
	    
 
	
	
}
