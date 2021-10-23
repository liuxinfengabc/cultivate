package com.lz.ht.configuration;


import com.lz.ht.filter.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Configuration
public class JwtConfig {


    @Bean
    public FilterRegistrationBean  basicFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        MyFilter myFilter = new MyFilter();
        registrationBean.setFilter(myFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/api/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
