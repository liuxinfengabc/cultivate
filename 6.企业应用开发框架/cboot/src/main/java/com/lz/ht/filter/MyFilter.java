package com.lz.ht.filter;

import com.lz.ht.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 */
@Slf4j
public class MyFilter  implements  Filter{

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //如果是login就直接放行
        String url = request.getRequestURL().toString();
        int index = url.lastIndexOf("/");
        boolean isApiLogin ="/login".equals(url.substring(index));
        if(isApiLogin){
            filterChain.doFilter(request,response);
            return ;
        }
        String xToken = request.getHeader("xToken");
        boolean verify = JwtUtil.verify(xToken);
        if(verify){
            filterChain.doFilter(request,response);
        }else{
            log.info("请求：{},notoken!",url);
            request.getRequestDispatcher("/error/noToken").forward(request,response);
        }
    }

}
