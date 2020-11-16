package com.lz.ht.base;

import ch.qos.logback.classic.Logger;
import com.lz.ht.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BaseController {



    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
    	 log.info(request.getRequestURI(),"Params:",getParameterMap().toString());
    }
    
    public Object getAttribute(String attributeName) {
        return this.getRequest().getAttribute(attributeName);
    }
    
    public void setAttribute(String attributeName, Object object) {
        this.getRequest().setAttribute(attributeName, object);
    }
    
    public Object getSession(String attributeName) {
        return this.getRequest().getSession(true).getAttribute(attributeName);
    }
    
    public void setSession(String attributeName, Object object) {
        this.getRequest().getSession(true).setAttribute(attributeName, object);
    }
    
    public HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) ra).getRequest();
    }
    
    public HttpServletResponse getResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) ra).getResponse();
    }
    
    public HttpSession getSession() {
        return this.getRequest().getSession(true);
    }
    
    public String getParameter(String paraName) {
        return this.getRequest().getParameter(paraName);
    }
    
    public Long getCurrentUserId() {
    	 Subject subject = SecurityUtils.getSubject();
         Session session = subject.getSession();
         Long loginUserId = (Long)session.getAttribute("loginUserId"); 
    	 return loginUserId;
    }
    
    /**
     * 获取表单格式数据(或url拼接参数)
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map getParameterMap() {
        return this.getRequest().getParameterMap();
    }
    
    public String getHeader(String headerName) {
        return this.getRequest().getHeader(headerName);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getHeaderMap() {
        Enumeration headerNames = this.getRequest().getHeaderNames();
        Map headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            String headerValue = getRequest().getHeader(headerName);
            headerMap.put(headerName, headerValue);
        }
        return headerMap;
    }
    
    public String getIpAddress() {
        // String ip = this.getRequest().getRemoteAddr();
        // return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
    
    /** * 获取服务器ip地址 * @return */
    public String getServerIpAddress() {
        InetAddress address;
        String serverIpAddress = null;
        try {
            address = InetAddress.getLocalHost(); // 获取的是本地的IP地址
            // PC-20140317PXKX/192.168.0.121
            serverIpAddress = address.getHostAddress();// 192.168.0.121
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return serverIpAddress;
    }
    
    /** * 获取json格式数据 * @return */ 
    @SuppressWarnings("unchecked") 
    public Map<String, Object> getRequestMap(){
        try { 
            InputStream inStream = this.getRequest().getInputStream(); //默认为json 
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream , "UTF-8")); 
            StringBuffer stringBuffer = new StringBuffer(); String buffer = ""; 
            while(null!=(buffer=(in.readLine()))){ 
                stringBuffer.append(buffer); 
            } 
            String reqDoc = stringBuffer.toString(); 
            if(reqDoc==null||reqDoc.equals("")){ 
                return null; 
            } 
            return JsonUtil.toMap(reqDoc) ;
        } catch (Exception e) { e.printStackTrace(); } return null; 
        
    } 
    
    /** * 允许跨域访问 */ 
    public void allowCrossDomainAccess(){ 
        HttpServletResponse servletResponse = getResponse(); 
        servletResponse.setHeader("Access-Control-Allow-Origin", "*"); 
        servletResponse.setHeader("Access-Control-Allow-Methods", "POST,GET"); 
        servletResponse.setHeader("Access-Control-Allow-Headers:x-requested-with", "content-type"); 
    } 
}