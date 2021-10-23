package com.lz.ht.constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.abel533.sql.SqlMapper;
import com.lz.ht.model.SysConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class SysConstant {

    /**
     * 公共盐
     */
    public static final String PUBLIC_SALT = "wxKYXuTPST5SG0jMQzVPsg==";
    
    /***
     * 用户初始密码
     */
    public static final String INIT_PASSWORD = "123456";
    
  
   
    /**平台RABC 资源菜单的 系统管理 resKey=1*/
    public static final Long SYSTEM_MANAGE_RESOURCE_RESKEY = 1L;
 
 
    /***
     * 获取平台配置信息
     */ 
    public  static List<SysConfig> SYSTEM_CONFIGS = new ArrayList<>();
    
    private SqlMapper sqlMapper;
    
    /***
     * 图片存放地址 如E:/image_root_path
     */
    private static  String IMAGE_ROOT_PATH ;

    
    public static final String IMAGE_ALLOW_EXT = ".jpg|.png|.gif|.bmp|.jpeg|.JPG|.PNG|.GIF|.BMP|.JPEG";

    /***
        *  文章存放地址 如E:/static_html_path
     */
    private static String STATIC_HTML_PATH;
    
    /***
     * Simditor富文本编辑器图片地址： E:/simditorImagePath
     */
    private static String SIMDITOR_IMAGE_PATH;
    
    
    public static String getIMAGE_ROOT_PATH() {
		return IMAGE_ROOT_PATH;
	}

	@Value("${systemConstant.imageRootPath}")
	public void setIMAGE_ROOT_PATH(String iMAGE_ROOT_PATH) {
		IMAGE_ROOT_PATH = iMAGE_ROOT_PATH;
	}

	public static String getSTATIC_HTML_PATH() {
		return STATIC_HTML_PATH;
	}
	
	@Value("${systemConstant.staticHtmlPath}")
	public   void setSTATIC_HTML_PATH(String sTATIC_HTML_PATH) {
		STATIC_HTML_PATH = sTATIC_HTML_PATH;
	}
	
	@Value("${systemConstant.simditorImagePath}")
	public  void setSIMDITOR_IMAGE_PATH(String sIMDITOR_IMAGE_PATH) {
		SIMDITOR_IMAGE_PATH = sIMDITOR_IMAGE_PATH;
	}
	 
	public static String getSIMDITOR_IMAGE_PATH() {
		return SIMDITOR_IMAGE_PATH;
	}
	

	public SqlMapper getSqlMapper() {
		return sqlMapper;
	}

    @Autowired
	public void setSqlMapper(SqlMapper sqlMapper) {
		this.sqlMapper = sqlMapper;
	}
    

    
    @PostConstruct
    public void init() {
    	SYSTEM_CONFIGS = sqlMapper.selectList(" select * from t_sys_config t",SysConfig.class);
    	log.info("***********SYSTEM_CONFIGS***********");
    	log.info(SYSTEM_CONFIGS.toString());
    	log.info("***********SYSTEM_CONFIGS***********");
    	createImageDir();
    	createStaticHtmlDir();
    	createSimditorImageDir();
    }
    
    
    private void createImageDir() {
    	File file = new File(IMAGE_ROOT_PATH);
    	try {
			if (!file.exists()) {
				file.mkdir();
			} 
		} catch (Exception e) {
			log.info(IMAGE_ROOT_PATH + ":无法创建图片存放路径");
		}
    }
    
    private void createStaticHtmlDir() {
    	File file = new File(STATIC_HTML_PATH);
    	try {
			if (!file.exists()) {
				file.mkdir();
			} 
		} catch (Exception e) {
			log.info(STATIC_HTML_PATH + ":无法创建静态HTML存放路径");
		}
    }
    
    
    private void createSimditorImageDir() {
    	File file = new File(SIMDITOR_IMAGE_PATH);
    	try {
			if (!file.exists()) {
				file.mkdir();
			} 
		} catch (Exception e) {
			log.info(SIMDITOR_IMAGE_PATH + ":无法创建Simditor富文本编辑器图片存放路径");
		}
    }
    
    
    public static String getConfigValueByName(String name) {
    	String value = null;
    	 for (SysConfig sysConfig : SYSTEM_CONFIGS) {
    		 if( sysConfig.getName().equals(name)) {
    			 value = sysConfig.getValue();
    			 break;
    		 }
		}
    	return value;
    }
    
 
    
    
}
