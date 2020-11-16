package com.lz.ht.configuration;

import com.lz.ht.base.dateconverter.DateConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** Web 配置
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Value("${systemConstant.imageRootPath}")
	private String localImageServerDir;

	@Value("${systemConstant.staticHtmlPath}")
	private String localStaticHtmlServerDir;

	@Value("${systemConstant.simditorImagePath}")
	private String simditorServerDir;
	
	
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
    }


    /***
     * 配置静态资源路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(!registry.hasMappingForPattern("/static/**")){
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
        registry.addResourceHandler("/localImage/image/**").addResourceLocations("file:///" + localImageServerDir + "/");
        registry.addResourceHandler("/simditor/image/**").addResourceLocations("file:///" + simditorServerDir + "/");
        registry.addResourceHandler("/localPage/staticHtml/**").addResourceLocations("file:///" + localStaticHtmlServerDir + "/");
        super.addResourceHandlers(registry);
    }

}